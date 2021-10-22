import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalajs.linker.interface.{
  OutputPatterns,
  ModuleKind,
  ModuleSplitStyle
}
import sbt._
import sbt.Keys._
import scala.sys.process._

object NextApp extends AutoPlugin {
  override def requires = ScalaJSPlugin
  override def trigger = noTrigger

  object autoImport {
    val npm = SettingKey[String]("npm", "The path to the npm executable")
    val scalaJsReactVersion =
      SettingKey[String](
        "scalaJsReactVersion",
        "The version of scalaJsReact to use"
      )
    val scalaJsReactUseGeneric = SettingKey[Boolean](
      "scalaJsReactUseGeneric",
      "Whether to use the 'core-generic' module scalajs-react, that provides effect agnositicism"
    )
    val nextServerProcess = AttributeKey[Option[Process]](
      "nextServerProcess",
      "A handle to the currently-running next dev server"
    )
    val startNextServer =
      TaskKey[StateTransform]("startNextServer", "npm run dev")
    val stopNextServer =
      TaskKey[StateTransform]("stopNextServer", "Stop running dev server")
    val nextServerIsRunning =
      TaskKey[Boolean]("nextServerIsRunning")
    val nextBuild =
      TaskKey[Int]("nextBuild", "npm run build")
    val npmInstall =
      TaskKey[Int]("npmInstall", "npm install")
  }

  import autoImport._
  override def projectSettings = Seq(
    scalacOptions += "-language:implicitConversions",
    fork := true,
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory := target.value / "js",
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory := target.value / "js",
    Compile / scalaSource := baseDirectory.value / "src",
    Test / scalaSource := baseDirectory.value / "test",
    scalaJSLinkerConfig ~= {
      // Enable ECMAScript module output.
      _.withModuleKind(ModuleKind.CommonJSModule)
      // Use .mjs extension.
        .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
        .withSourceMap(false)
    },
    (Compile / fastLinkJS / scalaJSLinkerConfig) ~= {
      _.withModuleSplitStyle(ModuleSplitStyle.SmallestModules)
    },
    scalaJsReactVersion := "2.0.0-RC4",
    scalaJsReactUseGeneric := false,
    libraryDependencies ++=
      (if (scalaJsReactUseGeneric.value) {
         Seq(
           "com.github.japgolly.scalajs-react" %%% "core-generic" % scalaJsReactVersion.value,
           "com.github.japgolly.scalajs-react" %%% "util-dummy-defaults" % scalaJsReactVersion.value
         )
       } else {
         Seq(
           "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReactVersion.value
         )
       }),
    npm := "npm",
    startNextServer := {
      if (!nextServerIsRunning.value) {
        val log = streams.value.log
        log.info(s"Starting Next dev server")
        val process =
          Process(Seq(npm.value, "run", "dev"), baseDirectory.value).run
        StateTransform(_.put(nextServerProcess, Some(process)))
      } else StateTransform(identity)
    },
    stopNextServer := {
      val log = streams.value.log
      state.value.get(nextServerProcess) match {
        case Some(Some(process)) if process.isAlive =>
          log.info("Stopped Next dev server")
          process.destroy()
          StateTransform(_.put(nextServerProcess, None))
        case _ =>
          log.info("No server is running")
          StateTransform(_.put(nextServerProcess, None))
      }
    },
    nextServerIsRunning := {
      state.value.get(nextServerProcess) match {
        case Some(Some(process)) => process.isAlive
        case _                   => false
      }
    },
    npmInstall := Process(Seq(npm.value, "install"), baseDirectory.value).!,
    nextBuild := Process(Seq(npm.value, "run", "build"), baseDirectory.value).!,
    nextBuild := nextBuild.dependsOn(Compile / fullLinkJS).value
  )
}
