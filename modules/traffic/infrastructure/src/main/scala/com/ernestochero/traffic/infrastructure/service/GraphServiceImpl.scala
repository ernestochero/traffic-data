package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.api.service.GraphService
import com.ernestochero.traffic.domain.{DataConfig, EdgeWeightedDigraph, Intersection, Measurement, TrafficMeasurement}
import com.ernestochero.traffic.domain.EdgeWeightedDigraphOps.EdgeWeightedDigraphOps
import com.ernestochero.traffic.domain.IntersectionOps._
import com.ernestochero.traffic.domain.MeasurementOps._
import io.circe.Decoder
import io.circe.parser.decode
import zio.{&, RIO, RLayer, Scope, ZIO, ZLayer}

import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object GraphService {
  val live: RLayer[Scope & DataConfig, GraphService[Scope]] = ZLayer {
    val prepareDecoder = Decoder[LazyList[TrafficMeasurement]].prepare(_.downField("trafficMeasurements"))
    for {
      config   <- ZIO.service[DataConfig]
      buffered <-
        ZIO.acquireRelease(ZIO.attempt(Source.fromFile(config.location)))(file => ZIO.succeedBlocking(file.close()))
      result   <- ZIO.fromEither(decode[LazyList[TrafficMeasurement]](buffered.mkString)(prepareDecoder))
    } yield new GraphServiceImpl[Scope](result)
  }
}
class GraphServiceImpl[R](trafficMeasurements: LazyList[TrafficMeasurement]) extends GraphService[R] {

  override def buildGraph: RIO[R, EdgeWeightedDigraph] =
    ZIO.attempt {
      trafficMeasurements.minBy(_.measurementTime).measurements.foldLeft(EdgeWeightedDigraph()) {
        (digraph, measurement) =>
          digraph.addEdge(measurement)
      }
    }

  private def validateIntersection(i: Intersection): Either[Throwable, Intersection] =
    i.getSecureVertex.toRight(new Throwable("Vertex not found")).map(_ => i)

  override def preProcessMeasurements(
    g: EdgeWeightedDigraph,
    i: Intersection
  ): RIO[R, (ArrayBuffer[Option[Measurement]], ArrayBuffer[Double])] =
    for {
      _     <- ZIO.fromEither(validateIntersection(i))
      tuple <- ZIO.attempt {
                 val size = g.adj.size
                 val edgeTo = mutable.ArrayBuffer.fill[Option[Measurement]](size)(None)
                 val distTo = mutable.ArrayBuffer.fill(size)(Double.PositiveInfinity)
                 val sourceV = i.getVertex
                 distTo(sourceV) = 0.0
                 val sourceDist = (sourceV, distTo(sourceV))
                 val sortByWeight: Ordering[(Int, Double)] = (a, b) => a._2.compareTo(b._2)
                 val queue = mutable.PriorityQueue[(Int, Double)](sourceDist)(sortByWeight)
                 while (queue.nonEmpty) {
                   val (minDestV, _) = queue.dequeue()
                   val in = minDestV.getIntersection
                   val edges = g.adj.getOrElse(in, List.empty)
                   edges.foreach { e =>
                     if (distTo(e.intersectionTo.getVertex) > distTo(e.intersectionFrom.getVertex) + e.transitTime) {
                       distTo(e.intersectionTo.getVertex) = distTo(e.intersectionFrom.getVertex) + e.transitTime
                       edgeTo(e.intersectionTo.getVertex) = Some(e)
                       if (!queue.exists(_._1 == e.intersectionTo.getVertex))
                         queue.enqueue((e.intersectionTo.getVertex, distTo(e.intersectionTo.getVertex)))
                     }
                   }
                 }
                 (edgeTo, distTo)
               }
    } yield tuple

}
