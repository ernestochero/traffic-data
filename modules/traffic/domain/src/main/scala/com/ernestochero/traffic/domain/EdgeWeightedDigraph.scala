package com.ernestochero.traffic.domain

import scala.collection.immutable.HashMap

case class EdgeWeightedDigraph(adj: HashMap[Intersection, List[Measurement]] = HashMap.empty)

object EdgeWeightedDigraphOps {
  implicit class EdgeWeightedDigraphOps(g: EdgeWeightedDigraph) {
    def addEdge(e: Measurement): EdgeWeightedDigraph = {
      val intersection = Intersection(e.startAvenue, e.startStreet)
      val list = g.adj.getOrElse(intersection, List.empty)
      val adj = g.adj + (intersection -> (list :+ e))
      EdgeWeightedDigraph(adj)
    }
  }
}
