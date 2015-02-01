
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
  jdbc,
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.1.1-2"
)

assemblyMergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) =>
    MergeStrategy.concat
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
    MergeStrategy.rename
  case PathList("META-INF", xs @ _*) =>
    (xs map {_.toLowerCase}) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.deduplicate
    }
      case _ => MergeStrategy.first
}

assemblyJarName in assembly := "rent-place.jar"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

