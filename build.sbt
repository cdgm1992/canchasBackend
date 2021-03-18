name := """play-scala-database"""
organization := "com.ceiba.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


scalaVersion := "2.11.0"

libraryDependencies += guice
libraryDependencies += evolutions
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
libraryDependencies += specs2 % Test

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.23"
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += "io.monix" %% "monix" % "3.3.0"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"


routesGenerator := InjectedRoutesGenerator
