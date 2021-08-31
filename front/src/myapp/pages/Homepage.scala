package myapp.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import myapp.Config
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("@src/myapp/pages/Homepage.module.css", JSImport.Namespace)
val styles: js.Dictionary[String] = js.native

val Homepage = ScalaComponent
  .builder[Unit]
  .render_(
    <.div(
      ^.className := styles("home"),
      <.h1(Config.title),
      Config.description.map(text => <.p(text, ^.key := text)).toVdomArray
    )
  )
  .build

@JSExportTopLevel("Homepage", "Homepage")
val HomePageJS = Homepage.toJsComponent.raw
