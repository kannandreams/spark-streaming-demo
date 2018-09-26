package com.kannandreams.examples
import com.kannandreams.examples.model.{GeoPoint, Rsvp}
import com.kannandreams.examples.websocket.MeetupReceiver
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.joda.time.{DateTime, DateTimeZone, Days, Hours}
import org.elasticsearch.spark._

import scala.collection.SortedMap
import java.text.SimpleDateFormat

import com.kannandreams.examples.util.ApplicationSettings
import com.typesafe.config.ConfigFactory

class StreamAggregateByInterval {

//  def start(sc: SparkContext, ssc: StreamingContext, websocket: String) {
def start(sc: SparkContext, ssc: StreamingContext, appSettings:ApplicationSettings,topValue:Int,window:Int) {

    //val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    val stream = ssc.receiverStream[Rsvp](new MeetupReceiver(appSettings.meetupWebSocketUrl, StorageLevel.MEMORY_ONLY_SER))
    //stream.checkpoint(Seconds(60))
    //stream.repartition(2)


    // Write the  RSVP events ( both yes and no ) for log analysis in Elastic Search
    stream.foreachRDD{(rdd,time) =>
      rdd.map(t=> {
        Map("event_name" -> t.event.event_name,
          "event_time" -> t.event.time,
          "response" -> t.response,
          "group_country" -> t.group.group_country,
          "group_city" -> t.group.group_city,
          "group_location" -> GeoPoint(t.group.group_lat,t.group.group_lon),
          "guests" -> t.guests,
          "group_topics" -> t.group.group_topics,
          "time" -> time
        )
      }).saveToEs("meetup/rsvp_stream_logs",Map("es.mapping.timestamp" -> "event_time"))}

    val countryRDD = sc.textFile("data/countries_data.csv")

    // Filter Accepted RSVP
    val rsvpYes = stream.filter(_.response == "yes").cache()

    // Count Yes Reponse by Cities and find the top N
    val rsvpCountByCity = rsvpYes
      .map(rsvp => (rsvp.group.group_city, 1))
      .reduceByKey(_ + _)

    rsvpCountByCity.print()


//    // Number of guests by Country
//    val rsvpByCountry = rsvpYes
//      .map(rsvp => (rsvp.group.group_country, rsvp.guests + 1))
//      .reduceByKey(_ + _)
//
//
//      //.map { case (country, attendees) => ("top",country, attendees) }
//
//
//    rsvpByCountry.foreachRDD{(rdd,time) =>
//      rdd.map(t=> {Map("top" -> t._1,"country" -> t._2,"attendess" -> t._3)})
//        .saveToEs("meetup/top_active_countries")}

//    rsvpByCountry.foreachRDD {
//      (country, attendees) => EsSpark.saveToEs("meetup/rsvp")
//    }

//      val joinedStream = rsvpByCountry.transform {
//        rdd => rdd.join(countryRDD)
//      }
    // rsvpByCountry.saveAsTextFiles("output/file.txt")

    // Trending Topics
//    val trendingTopics = rsvpYes
//      .flatMap( rsvp => rsvp.group.group_topics )
//      .map( topic => (topic.topic_name, 1) )
//      .reduceByKeyAndWindow((a:Int,b:Int) => a+b, Minutes(5), Seconds(10))
//      .filter( t => t._2 > 5 ) // min threshold = 5
//      .transform( (rdd, time) => rdd.map { case (topic, count) => ("trending",topic, count)} )

  val windowDuration = Minutes(window)

  val trendingTopics = rsvpYes
    .flatMap( rsvp => rsvp.group.group_topics )
    .map( topic => (topic.topic_name, 1) )
    .reduceByKeyAndWindow((a:Int,b:Int) => a+b, windowDuration, Seconds(10))
    .filter( t => t._2 > 5 ) // min threshold = 5
    //.transform( (rdd, time) => rdd.map { case (topic, count) => ("trending",topic, count)} )

  val sortedTrendingTopics = trendingTopics.transform(rdd =>
    rdd.sortBy(topics => topics._2,ascending = false)
  )

  sortedTrendingTopics.foreachRDD ( rdd => {
    val topList = rdd.take(topValue)
    topList.foreach{case (topic, count) => println("%s -> %d)".format(topic, count))}

    rdd.map(t => {
      Map("topic" -> t._1, "count" -> t._2)
    }).saveToEs("meetup/rsvp_trending_topics")

  }
  )
   //start the streaming operation and continue until killed.
    ssc.start()
    ssc.awaitTermination()
  }

  def updateFunction(values: Seq[Int], runningCount: Option[Int]): Some[Int] = {
    val newCount = values.sum + runningCount.getOrElse(0)
    Some(newCount)
  }


}



