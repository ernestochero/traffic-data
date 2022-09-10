package com.ernestochero.traffic.domain

import com.ernestochero.traffic.domain.Type.{Avenue, Street}
import com.ernestochero.traffic.domain.util.Utils.buildVertex
import io.circe._
import io.circe.generic.semiauto._

case class Intersection(avenue: Avenue, street: Street)
object Intersection {
  implicit val intersectionDecoder: Decoder[Intersection] = deriveDecoder
  implicit val intersectionEncoder: Encoder[Intersection] = deriveEncoder
}

object IntersectionOps {
  lazy val vertexMap: Map[Intersection, Int] = buildVertex
  implicit class IntersectionOps(i: Intersection) {
    def getVertex: Int = vertexMap(i)
    def getSecureVertex: Option[Int] = vertexMap.get(i)
  }
  implicit class IntOps(vertex: Int) {
    def getIntersection: Intersection =
      vertexMap.find(_._2 == vertex).get._1
  }
}
