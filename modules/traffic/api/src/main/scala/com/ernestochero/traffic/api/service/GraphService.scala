package com.ernestochero.traffic.api.service

import com.ernestochero.traffic.domain.{EdgeWeightedDigraph, Intersection, Measurement}
import zio.RIO

import scala.collection.mutable.ArrayBuffer

trait GraphService[R] {
  def buildGraph: RIO[R, EdgeWeightedDigraph]
  def preProcessMeasurements(
    g: EdgeWeightedDigraph,
    i: Intersection
  ): RIO[R, (ArrayBuffer[Option[Measurement]], ArrayBuffer[Double])]
}
