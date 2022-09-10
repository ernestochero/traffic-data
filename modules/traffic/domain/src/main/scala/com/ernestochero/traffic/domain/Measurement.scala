package com.ernestochero.traffic.domain

import com.ernestochero.traffic.domain.Type._
import io.circe._
import io.circe.generic.semiauto._

case class Measurement(
  startAvenue: Avenue,
  startStreet: Street,
  transitTime: Double,
  endAvenue: Avenue,
  endStreet: Street
)

object Measurement {
  implicit val measurementDecoder: Decoder[Measurement] =
    deriveDecoder
  implicit val measurementEncoder: Encoder[Measurement] =
    deriveEncoder
}

object MeasurementOps {
  implicit class MeasurementOps(m: Measurement) {
    def intersectionFrom: Intersection = Intersection(m.startAvenue, m.startStreet)
    def intersectionTo: Intersection = Intersection(m.endAvenue, m.endStreet)
  }
}
