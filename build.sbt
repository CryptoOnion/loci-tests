name := "Scala-Loci Smart"

organization := "de.tuda.stg"

version := "0.0.0"

scalaVersion := "2.13.2"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-Xlint")

// Required for Scala 2.13+
scalacOptions += "-Ymacro-annotations"

resolvers += Resolver.bintrayRepo("stg-tud", "maven")

val lociVer = "0.4.0-7-g02ad835"
libraryDependencies ++= Seq(
  "de.tuda.stg" %% "scala-loci-lang" % lociVer,
  "de.tuda.stg" %% "scala-loci-serializer-upickle" % lociVer,
  "de.tuda.stg" %% "scala-loci-communicator-tcp" % lociVer,
  "de.tuda.stg" %% "scala-loci-lang-transmitter-rescala" % lociVer
)
