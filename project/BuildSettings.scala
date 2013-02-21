import sbt._
import Keys._

object BuildSettings {
	val VERSION = "0.1-SNAPSHOT"

	lazy val baseSettings = seq (
		version 				:= VERSION,
		homepage 				:= None,
		organization 			:= "com.formalconcepts",
		organizationHomepage 	:= None,
		description 			:= "Typing Haskell, in Scala(z)",
		startYear 				:= Some(2013),
		scalaVersion 			:= "2.10.0",
		resolvers 				++= Dependencies.resolutionRepos,
		scalacOptions 			:= Seq(
			"-encoding", "utf8",
      		"-feature",
      		"-unchecked",
      		"-deprecation",
      		"-target:jvm-1.6",
      		"-language:postfixOps",
      		"-language:implicitConversions",
      		"-language:higherKinds",
      		"-Xlog-reflective-calls",
      		"-Ywarn-adapted-args"
		)
	)

}