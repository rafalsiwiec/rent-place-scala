
name := "Rent Place"

version := "0.1"

scalaVersion := "2.11.4"

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= Seq(
  //"org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.xerial" % "sqlite-jdbc" % "3.8.7",
  "com.typesafe.play" % "anorm_2.11" % "2.4.0-M2",
  "org.squeryl" % "squeryl_2.11" % "0.9.5-7",
  "com.typesafe.play" %% "play-slick" % "0.8.1",
  jdbc
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

