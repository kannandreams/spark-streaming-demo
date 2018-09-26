package com.kannandreams.examples
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.apache.spark.streaming.{ClockWrapper, Seconds, StreamingContext}
import org.apache.spark.SparkConf
import scala.util.Try
import org.apache.spark.rdd.RDD
import scala.reflect.io.Path
import scala.collection.mutable
import org.apache.hadoop.mapred.InvalidInputException


class MeetUpStreamingAppTest extends FlatSpec with Matchers with Eventually with BeforeAndAfter {

  private val master = "local[1]"
  private val appName = "RSVPSparkDemoApp-test"
  private val filePath: String = "./data/meetup.json"

  private var ssc: StreamingContext = _

  private val batchDuration = Seconds(1)

  var clock: ClockWrapper = _

  before {
    val conf = new SparkConf()
      .setMaster(master).setAppName(appName)
      .set("spark.streaming.clock", "org.apache.spark.streaming.util.ManualClock")

    ssc = new StreamingContext(conf, batchDuration)
    clock = new ClockWrapper(ssc)
  }

  after {
    if (ssc != null) {
      ssc.stop()
    }
    //Try(Path(filePath + "-1000").deleteRecursively)
  }

}