package com.ernestochero.traffic.api.service

import com.ernestochero.traffic.domain.{Intersection, Measurement}
import zio.RIO

trait ShortestPathService[R] {
  def pathTo(i: Intersection): RIO[R, List[Measurement]]
  def hasPath(i: Intersection): RIO[R, Boolean]
  def distTo(i: Intersection): RIO[R, Double]
}
