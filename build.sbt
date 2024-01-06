import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    //name := "BoyerMooreSearchTask"
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test

      //libraryDependencies += "org.scalatest" %% "scalatest" % "2.13.12" % Test,
    // Включите файлы с тестами, имеющими суффикс Test.scala
   // Test / includeFilter := "*test.scala"

)
