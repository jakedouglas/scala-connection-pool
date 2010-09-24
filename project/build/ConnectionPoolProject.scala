import sbt._
import Process._

class ConnectionPoolProject(info: ProjectInfo) extends DefaultProject(info) with rsync.RsyncPublishing with ruby.GemBuilding {
  val specs     = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5"
  val mockito   = "org.mockito" % "mockito-all" % "1.8.5"
  val pool      = "commons-pool" % "commons-pool" % "1.5.4" withSources() intransitive()

  /**
   * Gem build settings.
   */
  val gemAuthor             = "James Golick"
  val gemAuthorEmail        = "jamesgolick@gmail.com"
  override lazy val gemName = "connection_pool_java"

  /**
   * Include docs and source as build artifacts.
   */
  override def packageSrcJar = defaultJarPath("-sources.jar")
  val sourceArtifact = sbt.Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc, `package`)

  def rsyncRepo = "james@jamesgolick.com:/var/www/repo.jamesgolick.com"
}
