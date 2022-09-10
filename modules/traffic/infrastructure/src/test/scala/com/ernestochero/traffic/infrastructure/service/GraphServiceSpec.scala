package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.domain.{EdgeWeightedDigraph, Intersection, Measurement}
import com.ernestochero.traffic.infrastructure.service.GraphServiceTestData._
import zio._
import zio.test._
import zio.test.ZIOSpecDefault
import zio.test.Assertion._

import scala.collection.immutable.HashMap

object GraphServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("GraphService")(
    test("buildGraph") {
      val expectedResult =
        EdgeWeightedDigraph(
          HashMap(
            Intersection("A", "3") -> List(Measurement("A", "3", 1.0, "A", "4")),
            Intersection("A", "2") -> List(Measurement("A", "2", 1.0, "A", "3")),
            Intersection("A", "4") -> List(Measurement("A", "4", 1.0, "A", "5")),
            Intersection("A", "1") -> List(Measurement("A", "1", 1.0, "A", "2"))
          )
        )

      val graphServiceImpl = new GraphServiceImpl[Scope](graphServiceFakeData)
      assertZIO(graphServiceImpl.buildGraph.map(_.adj))(equalTo(expectedResult.adj))
    },
    test("preProcessMeasurements") {
      val g =
        EdgeWeightedDigraph(
          HashMap(
            Intersection("A", "3") -> List(Measurement("A", "3", 1.0, "A", "4")),
            Intersection("A", "2") -> List(Measurement("A", "2", 1.0, "A", "3")),
            Intersection("A", "4") -> List(Measurement("A", "4", 1.0, "A", "5")),
            Intersection("A", "1") -> List(Measurement("A", "1", 1.0, "A", "2")),
            Intersection("A", "5") -> List(Measurement("A", "5", 1.0, "A", "1"))
          )
        )
      val edgeToExpResult = Seq(
        None,
        Some(Measurement("A", "1", 1.0, "A", "2")),
        Some(Measurement("A", "2", 1.0, "A", "3")),
        Some(Measurement("A", "3", 1.0, "A", "4")),
        Some(Measurement("A", "4", 1.0, "A", "5"))
      )
      val distToExpResult = Seq(0.0, 1.0, 2.0, 3.0, 4.0)
      val graphServiceImpl = new GraphServiceImpl[Scope](graphServiceFakeData)
      for {
        result <- graphServiceImpl.preProcessMeasurements(g, Intersection("A", "1"))
        (edgeTo, distTo) = result
      } yield assert(edgeTo.toSeq)(equalTo(edgeToExpResult)) && assert(distTo.toSeq)(equalTo(distToExpResult))
    }
  )
}
