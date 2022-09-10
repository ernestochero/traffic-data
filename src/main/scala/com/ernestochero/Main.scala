package com.ernestochero

import com.ernestochero.traffic.api.service.TrafficService
import com.ernestochero.traffic.domain.{DataConfig, Input}
import com.ernestochero.traffic.infrastructure.service.{GraphService, ShortestPathService, TrafficService}
import io.circe.syntax._
import zio._
import zio.config.ReadError
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {
  val loggingLayer = SLF4J.slf4j(LogFormat.colored)
  val config: IO[ReadError[String], Config] = Config.load
  val dataConfigLayer: TaskLayer[DataConfig] = ZLayer.fromZIO(config.map(_.data))
  val dataInputLayer: TaskLayer[Input] = ZLayer.fromZIO(config.map(_.input))

  val graphServiceLayer =
    dataConfigLayer >+> GraphService.live
  val shortestPathServiceLayer =
    (dataInputLayer ++ graphServiceLayer) >+> ShortestPathService.live
  val trafficServiceLayer =
    shortestPathServiceLayer >+> TrafficService.live

  val app =
    for {
      _              <- ZIO.logInfo("Traffic Data Implementation")
      trafficService <- ZIO.service[TrafficService[Scope]]
      dataInput      <- ZIO.service[Input]
      result         <- trafficService.calculateBestRoute(dataInput)
      jsonResult = result.asJson
      _              <- ZIO.logInfo(s"Best Route result: $jsonResult")
      _              <- ZIO.logInfo("Traffic Data Implementation Finalized")
    } yield ()
  override def run: ZIO[Environment with Scope, Throwable, Unit] =
    app.provideSomeLayer(loggingLayer ++ trafficServiceLayer ++ dataInputLayer)
}
