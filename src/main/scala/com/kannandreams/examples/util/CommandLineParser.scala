package com.kannandreams.examples.util


import com.typesafe.config.ConfigFactory


object CommandLineParser {

  val appSettings = new ApplicationSettings
  var nvalue = appSettings.topicTopN
  var window = appSettings.trendingTopicWindow


  def main(args: Array[String]): Unit = {
    parse("-top 5 -minutes 5 ".split(" ").toList)
  }

  val usage =
    s"""
  This is a Meetup Spark Streaming application receives data from Meetup websocket source.
  It process the data and provide some insights like trending topics,top Countries,etc.
  You can monitor the processed streams in real-time graph using Elastic Search and Kibana.
  You can customize by passing command line parameters to filter top N trending topics in X amount of time.
  Usage: spark-submit <<RSVPSparkDemo*>>.jar [options]
    Options:
    -h, --help
    -t, --top <N>  Default: ${appSettings.topicTopN}
    -m, --minutes <N>  Default: ${appSettings.trendingTopicWindow}
  """


  def parse(list: List[String]): this.type = {

    list match {
      case Nil => this

      case ("--top" | "-t") :: value :: tail => {
        nvalue = value.toInt
        parse(tail)
      }

      case ("--minutes" | "-m") :: value :: tail => {
        window = value.toInt
        parse(tail)
      }

      case ("--help" | "-h") :: tail => {
        printUsage(0)
      }
      case _ => {
        printUsage(1)
      }
    }
  }

  def printUsage(exitNumber: Int) = {
    println(usage)
    sys.exit(status = exitNumber)
  }

}
