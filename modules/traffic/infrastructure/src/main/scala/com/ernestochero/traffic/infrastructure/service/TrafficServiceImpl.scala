package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.api.service.{ShortestPathService, TrafficService}
import com.ernestochero.traffic.domain.{Input, Intersection, Output}
import zio._

object TrafficService {
  val live: RLayer[Scope & ShortestPathService[Scope], TrafficService[Scope]] = ZLayer {
    for {
      shortestService <- ZIO.service[ShortestPathService[Scope]]
    } yield new TrafficServiceImpl[Scope](shortestService)
  }
}
class TrafficServiceImpl[R](s: ShortestPathService[R]) extends TrafficService[R] {
  override def calculateBestRoute(dataInput: Input): RIO[R, Output] = {
    val startIntersection = Intersection(dataInput.startAvenue, dataInput.startStreet)
    val endIntersection = Intersection(dataInput.endAvenue, dataInput.endStreet)
    for {
      route       <- s.pathTo(endIntersection)
      transitTime <- s.distTo(endIntersection)
    } yield Output(startIntersection, endIntersection, route, transitTime)
  }
}
