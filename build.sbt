ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "boyer-moore-search",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
  )
