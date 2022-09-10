package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.domain.{Intersection, Measurement}
import zio._
import zio.test._
import zio.test.Assertion._
import com.ernestochero.traffic.infrastructure.service.ShortestPathServiceTestData._

object ShortestPathServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("ShortestPathService")(
    test("pathTo") {
      val measurementsExpResult =
        List(
          Measurement("A", "1", 1.0, "A", "2"),
          Measurement("A", "2", 1.0, "A", "3"),
          Measurement("A", "3", 1.0, "A", "4"),
          Measurement("A", "4", 1.0, "A", "5"))
      val shortestPathServiceImpl = new ShortestPathServiceImpl[Scope](edgeTo, distTo)
      assertZIO(shortestPathServiceImpl.pathTo(Intersection("A", "5")))(equalTo(measurementsExpResult))
    },
    test("hasPath") {
      val shortestPathServiceImpl = new ShortestPathServiceImpl[Scope](edgeTo, distTo_2)
      assertZIO(shortestPathServiceImpl.hasPath(Intersection("A", "5")))(equalTo(false))
      assertZIO(shortestPathServiceImpl.hasPath(Intersection("A", "1")))(equalTo(true))
    },
    test("distTo") {
      val shortestPathServiceImpl = new ShortestPathServiceImpl[Scope](edgeTo, distTo)
      assertZIO(shortestPathServiceImpl.distTo(Intersection("A", "5")))(equalTo(4.0))
    }
  )
}
