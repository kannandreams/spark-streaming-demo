#!/usr/bin/env python

# Kafka Library
from kafka import SimpleProducer, KafkaClient

# https://github.com/liris/websocket-client
from websocket import create_connection

# Kafka Producer
kafka = KafkaClient("localhost:9092")
producer = SimpleProducer(kafka)

def rsvp_source():
    ws = create_connection('ws://stream.meetup.com/2/rsvps')
    while True:
        try:
            rsvp_data = ws.recv() # Get realtime data using web socketss
            if rsvp_data:
                # Send the stream to the topic "rsvp_stream"
                producer.send_messages("rsvp_stream", rsvp_data)
        # No matter what the Exception is keep calling the function recursively
        except:
            rsvp_source()

if __name__ == '__main__':
    rsvp_source()