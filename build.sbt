name := "scalaseed"

version := "1.0"

scalaVersion := "2.11.8"

// NOTE:  importing Play library dependencies!
libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.9.29",
  "com.typesafe.play" %% "play" % "2.5.2",
  "com.typesafe.play" %% "play-test" % "2.5.2",
  "com.typesafe.play" %% "play-ws" % "2.5.2"
)