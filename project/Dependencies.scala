import sbt._

object Dependencies {

  val resolutionRepos = Seq(
    "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")


  val scalaz        = "org.scalaz"          %%  "scalaz-core"       % "7.0.0-M7"   
  val shapeless     = "com.chuusai"         %   "shapeless"         % "1.2.3"       cross CrossVersion.full
  val specs2        = "org.specs2"          %%  "specs2"            % "1.13"
  val junit         = "junit"               %   "junit"             % "4.7"           

}
