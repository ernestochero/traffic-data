package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.domain.Measurement

object ShortestPathServiceTestData {
  val edgeTo = Seq(
    None,
    Some(Measurement("A", "1", 1.0, "A", "2")),
    Some(Measurement("A", "2", 1.0, "A", "3")),
    Some(Measurement("A", "3", 1.0, "A", "4")),
    Some(Measurement("A", "4", 1.0, "A", "5"))
  )
  val distTo = Seq(0.0, 1.0, 2.0, 3.0, 4.0)
  val distTo_2 = Seq(0.0, 1.0, 2.0, 3.0, Double.PositiveInfinity)
}
