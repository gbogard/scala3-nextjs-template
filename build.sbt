ThisBuild / scalaVersion := "3.0.1"

val circeVersion = "0.14.1"

lazy val root = project
  .in(file("."))
  .disablePlugins(RevolverPlugin)
  .aggregate(front, server)
  .settings(
    name := "scala3-nextjs"
  )

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(
    name := "shared",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion
    )
  )

lazy val server = project
  .in(file("server"))
  .dependsOn(shared.jvm)

lazy val front = project
  .in(file("front"))
  .enablePlugins(NextApp)
  .disablePlugins(RevolverPlugin)
  .dependsOn(shared.js)
