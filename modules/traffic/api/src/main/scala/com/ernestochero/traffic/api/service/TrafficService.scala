package com.ernestochero.traffic.api.service

import com.ernestochero.traffic.domain.{Input, Output}
import zio.RIO

trait TrafficService[R] {
  def calculateBestRoute(dataInput: Input): RIO[R, Output]
}
