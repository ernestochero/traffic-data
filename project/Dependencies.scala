import sbt._
object Versions {
  val scala2 = "2.13.8"
  val zio = "2.0.2"
  val zioConfig = "3.0.2"
  val zioLogging = "2.1.0"
  val zioPrelude = "1.0.0-RC14"
  val scalatest = "3.2.12"
  val logbackClassic = "1.2.11"
  val circe = "0.14.2"
  val scalafixOrganizeImports = "0.6.0"
}

object Dependencies {
  lazy val zio = Seq(
    "dev.zio" %% "zio" % Versions.zio
  )
  lazy val zioConfig = Seq(
    "dev.zio" %% "zio-config"          % Versions.zioConfig,
    "dev.zio" %% "zio-config-typesafe" % Versions.zioConfig,
    "dev.zio" %% "zio-config-magnolia" % Versions.zioConfig
  )
  lazy val zioLogging = Seq(
    "dev.zio"       %% "zio-logging"       % Versions.zioLogging,
    "dev.zio"       %% "zio-logging-slf4j" % Versions.zioLogging,
    "ch.qos.logback" % "logback-classic"   % Versions.logbackClassic
  )

  lazy val zioStream = Seq(
    ("dev.zio" %% "zio-streams" % Versions.zio)
  )

  lazy val zioPrelude = Seq(
    "dev.zio" %% "zio-prelude" % Versions.zioPrelude
  )
  lazy val circe = Seq(
    "io.circe" % "circe-core_2.13"    % Versions.circe,
    "io.circe" % "circe-generic_2.13" % Versions.circe,
    "io.circe" % "circe-parser_2.13"  % Versions.circe
  )
  lazy val scalatest = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest
  )

  lazy val zioTest = Seq(
    "dev.zio" %% "zio-test"     % Versions.zio % "test",
    "dev.zio" %% "zio-test-sbt" % Versions.zio % "test"
  )
}
