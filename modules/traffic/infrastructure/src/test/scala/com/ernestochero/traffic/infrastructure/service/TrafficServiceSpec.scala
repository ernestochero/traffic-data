package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.domain.Input
import com.ernestochero.traffic.infrastructure.service.ShortestPathServiceTestData._
import io.circe.Json
import zio._
import zio.test._
import zio.test.Assertion._
import io.circe.syntax._
import io.circe.parser._

object TrafficServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("TrafficService")(
    test("calculateBestRoute") {
      val shortestPathServiceImpl = new ShortestPathServiceImpl[Scope](edgeTo, distTo)
      val trafficServiceImpl = new TrafficServiceImpl[Scope](shortestPathServiceImpl)
      val jsonExpStr: String =
        """
          {
            "startIntersection" : {
              "avenue" : "A",
              "street" : "1"
            },
            "endIntersection" : {
              "avenue" : "A",
              "street" : "5"
            },
            "measurements" : [
              {
                "startAvenue" : "A",
                "startStreet" : "1",
                "transitTime" : 1.0,
                "endAvenue" : "A",
                "endStreet" : "2"
              },
              {
                "startAvenue" : "A",
                "startStreet" : "2",
                "transitTime" : 1.0,
                "endAvenue" : "A",
                "endStreet" : "3"
              },
              {
                "startAvenue" : "A",
                "startStreet" : "3",
                "transitTime" : 1.0,
                "endAvenue" : "A",
                "endStreet" : "4"
              },
              {
                "startAvenue" : "A",
                "startStreet" : "4",
                "transitTime" : 1.0,
                "endAvenue" : "A",
                "endStreet" : "5"
              }
            ],
            "totalTransitTime" : 4.0
          }
        """
      val jsonExp: Json = parse(jsonExpStr).getOrElse(Json.Null)
      val input = Input("A", "1", "A", "5")
      assertZIO(trafficServiceImpl.calculateBestRoute(input).map(_.asJson))(equalTo(jsonExp))
    }
  )
}
