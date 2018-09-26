Meetup RSVP Spark Streaming Application Demo



To deploy Spark application :

git clone https://github.com/kannandreams/spark-streaming-demo.git
cd spark-streaming-demo
sbt assembly

=============================

spark-submit --class com.kannandreams.examples.MeetUpStreamingApp \
./target/scala-2.11/RSVPSparkDemo-assembly-1.0.jar \
--top 5
--minutes 10


==========================================================

to delete the index in elastic search

curl -XDELETE localhost:9200/meetup
