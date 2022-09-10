import Settings._
import ModuleSettings._
import Dependencies._

lazy val trafficDomain = domainProject("traffic").minimalSettings
  .settings(
    libraryDependencies := circe
  )

lazy val trafficApi = apiProject("traffic").minimalSettings
  .dependsOn(trafficDomain % "compile->compile;test->test")
  .settings(
    libraryDependencies := zio ++ zioConfig ++ zioLogging
  )

lazy val trafficInfra = infraProject("traffic").minimalSettings
  .dependsOn(trafficDomain % "compile->compile;test->test")
  .dependsOn(trafficApi % "compile->compile;test->test")
  .settings(
    libraryDependencies := zioStream ++ zioTest,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

lazy val root = (project in file(".")).minimalSettings
  .aggregate(
    trafficDomain,
    trafficApi,
    trafficInfra
  )
  .dependsOn(trafficApi, trafficDomain, trafficInfra)
  .settings(
    name := "traffic-backend",
    version := "0.0.1-SNAPSHOT",
    Compile / run / mainClass := Some("com.ernestochero.Main")
  )

addCommandAlias("fix", "; scalafix; Test / scalafix")
addCommandAlias("fmt", "; scalafmt; scalafmtSbt; Test / scalafmt")
addCommandAlias("fixCheck", "; scalafix --check; Test / scalafix --check")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; Test / scalafmtCheck")
