package com.kannandreams.examples.util

import com.typesafe.config.{Config, ConfigFactory}


final class ApplicationSettings {

  protected val conf: Config = ConfigFactory.load.getConfig("meetup-app")

  val meetupWebSocketUrl: String = conf.getString("meetup.websocket.url")
  val sparkMaster: String = conf.getString("spark.master")
  val StreamingBatchInterval: Int = conf.getInt("spark.streaming.batch.interval")
  val appName:String = conf.getString("spark.app.name")

  val esResource:String = conf.getString("es.resource")
  val esIndexAutoCreate:String = conf.getString("es.index.auto.create")
  val esNodes:String = conf.getString("es.nodes")
  val esPort:String = conf.getString("es.port")

  val topicTopN:Int = conf.getInt("trending.topics.top")
  val trendingTopicWindow:Int =  conf.getInt("trending.topics.minute")


}
