import org.scalajs.linker.interface.{OutputPatterns, ModuleKind, ModuleSplitStyle}

val scalaJsReactVersion = "2.0.0-RC3"

ThisBuild / scalaVersion := "3.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-nextjs",
  )

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(name := "shared")
  .jvmSettings()
  .jsSettings()

lazy val backend = project
  .in(file("backend"))
  .dependsOn(shared.jvm)

lazy val front = project
  .in(file("front"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "scala3-nextjs",
    scalacOptions += "-language:implicitConversions",
    (Compile / fastLinkJS / scalaJSLinkerOutputDirectory) := target.value / "js",
    (Compile / fullLinkJS / scalaJSLinkerOutputDirectory) := target.value / "js",
    scalaJSLinkerConfig ~= {
      // Enable ECMAScript module output.
      _.withModuleKind(ModuleKind.CommonJSModule)
      // Use .mjs extension.
       .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
       .withSourceMap(false)
    },
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReactVersion
    )
  )
  .dependsOn(shared.js)


