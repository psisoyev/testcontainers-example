name := "testcontainers-munit"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies += "org.scalameta" %% "munit" % "0.7.4" % Test

libraryDependencies ++= List(
  "testcontainers-scala-munit",
  "testcontainers-scala-pulsar",
  "testcontainers-scala-mockserver"
).map("com.dimafeng" %% _ % "0.37.0" % Test)

libraryDependencies ++= List(
  "pulsar4s-avro",
  "pulsar4s-core"
).map("com.sksamuel.pulsar4s" %% _ % "2.4.6")