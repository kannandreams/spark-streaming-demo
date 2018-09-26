Meetup RSVP Spark Streaming Application Demo

Real time spark streaming over Meetup websocket
Apache Spark Streaming Framework used to process the real time events from Meetup API Stream

Background

util/ApplicationSettings.Scala

separation of configuration from code.it is easy to customize according to the environment we are running the app.


Architecture



Operation Steps

Pre-requests

Building

References


to delete the index in elastic search

curl -XDELETE localhost:9200/meetup

To deploy Spark application :

git clone <<git url>>
cd RSVPSparkDemo
sbt assembly run

spark-submit --class com.kannandreams.examples.MeetUpStreamingApp ./target/scala-2.11/RSVPSparkDemo-assembly-1.0.jar \
-Dtrendingtopics.topN=5 > results.txt 2>&1


Input options:

-Dspark.master - Autodetect in DSE. Specify for non-DSE deployments.
-Dspark.cassandra.connection.host - Default is 127.0.0.1, replace with rpc_address of one of the nodes.
-Dspark.cores.max - Default configured is 2
see resources/applicaiton.conf for more
