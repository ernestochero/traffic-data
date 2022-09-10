package com.ernestochero.traffic.domain.util

import com.ernestochero.traffic.domain.Intersection

object Utils {
  def buildVertex: Map[Intersection, Int] =
    (
      for {
        avenue <- 0 to 20
        street <- 1 to 30
        i = Intersection(s"${(65 + avenue).toChar}", s"$street")
        id = ((avenue * 30) + street) - 1
      } yield i -> id
    ).toMap
}
