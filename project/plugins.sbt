// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

// Compile coffeescript to js
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
