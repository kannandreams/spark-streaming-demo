package com.kannandreams.examples.model

case class Rsvp(
                 rsvp_id: Long,
                 response: String,
                 guests: Int,
                 mtime: Long,
                 visibility : String,
                 event: RsvpEvent,
                 group: RsvpGroup,
                 member: RsvpMember,
                 venue: RsvpVenue
               )

case class RsvpEvent(
                        event_id: String,
                        event_name: Option[String],
                        event_url: Option[String],
                        time: Option[Long]
                      )

case class RsvpGroupTopics(
                              topic_name: Option[String],
                              urlkey: Option[String]
                            )

case class RsvpGroup(
                        group_id: Long,
                        group_name: String,
                        group_city: Option[String],
                        group_country: Option[String],
                        group_state: Option[String],
                        group_urlname: Option[String],
                        group_lat: Option[String],
                        group_lon: Option[String],
                        group_topics: List[RsvpGroupTopics]
                      )

case class RsvpMember(
                         member_id: Long,
                         member_name: Option[String],
                         other_services: Option[String],
                         photo: Option[String]
                       )

case class RsvpVenue(
                        venue_id: Long,
                        venue_name: Option[String],
                        lat: Option[String],
                        lon: Option[String]
                      )

case class GeoPoint(lat: Option[String], lon: Option[String])
