package com.kannandreams.examples.websocket

import com.kannandreams.examples.model.Rsvp
import org.apache.spark.internal.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.jfarcand.wcs.{MessageListener, TextListener, WebSocket}
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Custom receiver for WebSocket
  */
class MeetupReceiver(url: String, storageLevel: StorageLevel)
  extends Receiver[Rsvp](storageLevel) with Runnable with Logging {

  private var webSocket: WebSocket = _

  @transient
  private var thread: Thread = _

  override def onStart(): Unit = {
    thread = new Thread(this)
    thread.start()
  }

  override def onStop(): Unit = {
    setWebSocket(null)
    thread.interrupt()
    logInfo("WebSocket receiver stopped")
  }

  override def run(): Unit = {
    println("Received ----")
    receive()
  }

  private def receive(): Unit = {

    val connection = WebSocket().open(url)
    println("WebSocket  Connected ..." )
    println("Connected ------- " + connection)
    setWebSocket(connection)

    connection.listener(new TextListener {
      override def onMessage(message: String) {
        // System.out.println("Message in Spark client is --> " + message)
        parseJson(message)
      }
    })

  }

  private def setWebSocket(newWebSocket: WebSocket): Unit = synchronized {
    if (webSocket != null) {
      webSocket.shutDown
    }
    webSocket = newWebSocket
  }

  private def parseJson(jsonStr: String): Unit =
  {
    implicit lazy val formats: DefaultFormats.type = DefaultFormats

    try {
      val json = parse(jsonStr)
      val rsvp = json.extract[Rsvp]
      store(rsvp)
    } catch {
      case e: MappingException => logError("Invalid JSON to map to Rsvp  :" + e.msg)
      case e: Exception => logError("Invalid JSON to map to Rsvp")
    }

  }

}