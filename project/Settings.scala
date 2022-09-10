import sbt._
import Keys._
object Settings {
  private lazy val baseCompilerOptions = Seq(
    "-deprecation",    // emit warning and location for usages of deprecated APIs
    "-feature",        // emit warning and location for usages of features that should be imported explicitly
    "-unchecked",      // enable additional warnings where generated code depends on assumptions
    "-Xfatal-warnings" // fail the compilation if there are any warnings
  )

  implicit final class ProjectOps(val project: Project) extends AnyVal {
    def minimalSettings: Project =
      project.scala2Settings.scalafixSettings.noCoverageSettings
        .settings(
          organization := "com.ernestochero",
          organizationName := "TrafficData"
        )

    def scala2Settings: Project =
      project.settings(
        scalacOptions := baseCompilerOptions,
        scalaVersion := Versions.scala2
      )

    def scalafixSettings: Project = {
      import scalafix.sbt.ScalafixPlugin.autoImport._

      project.settings(
        semanticdbEnabled := true,
        semanticdbTargetRoot := target.value / "semanticdb",
        ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % Versions.scalafixOrganizeImports
      )
    }

    def itSettings: Project = project
      .configs(IntegrationTest)
      .settings(Defaults.itSettings)
      .settings(IntegrationTest / fork := true)

    def noCoverageSettings: Project =
      project
        .disablePlugins(scoverage.ScoverageSbtPlugin)

    def zioTestSettings: Project =
      project
        .settings(
          testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"), // Use zio-test runner
          run / fork := true                                                  // Ensure canceling `run` releases socket, no matter what
        )

  }
}

