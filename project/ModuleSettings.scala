import sbt.Keys._
import sbt._

object ModuleSettings {

  def domainProject(projectName: String): Project =
    module(s"$projectName-domain", s"$projectName/domain")

  def apiProject(projectName: String): Project =
    module(s"$projectName-api", s"$projectName/api")

  def infraProject(projectName: String): Project =
    module(s"$projectName-infrastructure", s"$projectName/infrastructure")

  def commonProject(projectName: String): Project =
    module(s"common-$projectName", s"common/$projectName")

  private def module(projectName: String, filePath: String): Project =
    sbt
      .Project(projectName, file(s"./modules/$filePath"))
      .settings(name := projectName)

}
