package com.ernestochero

import com.ernestochero.traffic.domain.{DataConfig, Input}
import zio.IO
import zio.config._
import zio.config.typesafe._

object Config {
  private val source = TypesafeConfigSource.fromResourcePath

  def load: IO[ReadError[String], Config] =
    zio.config.read(zio.config.magnolia.descriptor[Config] from source)
}

final case class Config(data: DataConfig, input: Input)
