package com.ernestochero.traffic.domain

import io.circe._
import io.circe.generic.semiauto._

case class Output(
  startIntersection: Intersection,
  endIntersection: Intersection,
  measurements: List[Measurement],
  totalTransitTime: Double
)

object Output {
  implicit val outputDecoder: Decoder[Output] = deriveDecoder
  implicit val outputEncoder: Encoder[Output] = deriveEncoder
}
