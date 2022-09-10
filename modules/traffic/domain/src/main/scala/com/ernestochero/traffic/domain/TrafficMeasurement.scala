package com.ernestochero.traffic.domain

import io.circe._
import io.circe.generic.semiauto._

case class TrafficMeasurement(
  measurementTime: Long,
  measurements: LazyList[Measurement]
)
object TrafficMeasurement {
  implicit val trafficMeasurementDecoder: Decoder[TrafficMeasurement] =
    deriveDecoder
  implicit val trafficMeasurementEncoder: Encoder[TrafficMeasurement] =
    deriveEncoder
}
