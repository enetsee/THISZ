import sbt._
import sbt.Keys._

object ThiszBuild extends Build {

  import BuildSettings._
  import Dependencies._
  
  lazy val thisz = Project("thisz",file("."))  
     .settings(baseSettings: _*)
     .settings( libraryDependencies ++= 
        provided(scalaz) ++
        test(specs2,junit)
      )
}
