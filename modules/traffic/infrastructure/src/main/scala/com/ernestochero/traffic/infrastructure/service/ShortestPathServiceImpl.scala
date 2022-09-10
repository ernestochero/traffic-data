package com.ernestochero.traffic.infrastructure.service

import com.ernestochero.traffic.api.service.{GraphService, ShortestPathService}
import com.ernestochero.traffic.domain.{EdgeWeightedDigraph, Input, Intersection, Measurement}
import com.ernestochero.traffic.domain.IntersectionOps._
import com.ernestochero.traffic.domain.MeasurementOps._
import zio._

import scala.annotation.tailrec

object ShortestPathService {
  val live: RLayer[Scope & Input & GraphService[Scope], ShortestPathService[Scope]] = ZLayer {
    for {
      graphService <- ZIO.service[GraphService[Scope]]
      dataInput    <- ZIO.service[Input]
      graph        <- graphService.buildGraph
      result       <- graphService.preProcessMeasurements(
                        graph,
                        Intersection(dataInput.startAvenue, dataInput.startStreet)
                      )
      (edgeTo, distTo) = result
    } yield new ShortestPathServiceImpl(edgeTo.toSeq, distTo.toSeq)
  }
}
class ShortestPathServiceImpl[R](edgeTo: Seq[Option[Measurement]], distTo: Seq[Double]) extends ShortestPathService[R] {
  override def pathTo(i: Intersection): RIO[R, List[Measurement]] = {
    @tailrec
    def go(list: List[Measurement], vv: Int): List[Measurement] =
      edgeTo(vv) match {
        case Some(e) => go(e +: list, e.intersectionFrom.getVertex)
        case None    => list
      }
    hasPath(i).map(b => if (!b) List.empty else go(List(), i.getVertex))
  }

  override def hasPath(i: Intersection): RIO[R, Boolean] = {
    val v = i.getVertex
    ZIO.fromEither(distTo.lift(v).map(_ < Double.PositiveInfinity).toRight(new Throwable(s"Vertex $v does not exist")))
  }

  override def distTo(i: Intersection): RIO[R, Double] = {
    val v = i.getVertex
    ZIO.fromEither(distTo.lift(v).toRight(new Throwable(s"Vertex $v does not exist")))
  }

}
