//name := "RSVPSparkDemo"
//
//version := "0.1"
//
//scalaVersion := "2.11.4"

lazy val root = (project in file(".")).
  settings(
    name := "RSVPSparkDemo",
    version := "1.0",
    scalaVersion := "2.11.4",
    mainClass in Compile := Some("com.kannandreams.examples.MeetUpStreamingApp")
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.3.0",
  "org.apache.spark" %% "spark-streaming" % "2.3.0",
  "org.apache.spark" %% "spark-sql" % "2.3.0",
  // "org.slf4j" % "slf4j-api" % "1.7.10",
  "org.jfarcand" % "wcs" % "1.5",
  "com.typesafe" % "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  //"org.elasticsearch" %% "elasticsearch-spark" % "2.3.3",
  "org.elasticsearch" %% "elasticsearch-spark-20" % "6.2.4"
)
//
//import sbtassembly.MergeStrategy
//assemblyMergeStrategy in assembly := {
//  case PathList("org", "apache", xs @ _*)  => MergeStrategy.last
//  case PathList("org", "elasticsearch", xs @ _*)  => MergeStrategy.first
//  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
//  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
//  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
//  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
//  case PathList("com", "google", xs @ _*) => MergeStrategy.last
//  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
//  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
//  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
//  case x =>
//    val oldStrategy = (assemblyMergeStrategy in assembly).value
//    oldStrategy(x)
//}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}