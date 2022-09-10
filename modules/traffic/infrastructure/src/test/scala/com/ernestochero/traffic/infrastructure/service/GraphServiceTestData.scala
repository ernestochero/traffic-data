package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.domain.{Measurement, TrafficMeasurement}

object GraphServiceTestData {
  val graphServiceFakeData =
    LazyList(
      TrafficMeasurement(
        2,
        LazyList(
          Measurement("A", "1", 2, "A", "2"),
          Measurement("A", "2", 2, "A", "3"),
          Measurement("A", "3", 2, "A", "4"),
          Measurement("A", "4", 2, "A", "5")
        )
      ),
      TrafficMeasurement(
        1,
        LazyList(
          Measurement("A", "1", 1, "A", "2"),
          Measurement("A", "2", 1, "A", "3"),
          Measurement("A", "3", 1, "A", "4"),
          Measurement("A", "4", 1, "A", "5")
        )
      ),
      TrafficMeasurement(
        3,
        LazyList(
          Measurement("A", "1", 3, "A", "2"),
          Measurement("A", "2", 3, "A", "3"),
          Measurement("A", "3", 3, "A", "4"),
          Measurement("A", "4", 3, "A", "5")
        )
      )
    )
}
