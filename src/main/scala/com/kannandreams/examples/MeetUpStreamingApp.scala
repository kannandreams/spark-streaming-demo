package com.kannandreams.examples
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.kannandreams.examples.util.{ApplicationSettings,CommandLineParser}

object MeetUpStreamingApp {

  // Spark Application Configuration
  val appSettings = new ApplicationSettings

  def main(args: Array[String]): Unit = {

    CommandLineParser.parse(args.toList)


    println("Application Settings Custom Input")
    println("Default Top N ----> " + appSettings.topicTopN)
    println("Input Top N --> " + CommandLineParser.nvalue)
    println("Input Window --> " + CommandLineParser.window)
    println("Spark Master ----> " + appSettings.sparkMaster)
    println("ES Resource ----> " + appSettings.esResource)


    val conf = new SparkConf(true)
      .setMaster(appSettings.sparkMaster)
      .setAppName(appSettings.appName)
      // Define Elastic search properties
      .set("es.resource", appSettings.esResource)
      .set("es.index.auto.create", appSettings.esIndexAutoCreate)
      .set("es.nodes", appSettings.esNodes)
      .set("es.port", appSettings.esPort)

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(appSettings.StreamingBatchInterval))
    val stream = new StreamAggregateByInterval
    stream.start(sc,ssc, appSettings,CommandLineParser.nvalue,CommandLineParser.window)

  }
}
