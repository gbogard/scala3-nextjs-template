package myapp.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import myapp.Config
import scala.scalajs.js.annotation.JSExportTopLevel

val Homepage = ScalaComponent
  .builder[Unit]
  .render_(
    <.div(
      <.h1(Config.title),
      Config.description.map(text => <.p(text, ^.key := text)).toVdomArray
    )
  )
  .build

@JSExportTopLevel("Homepage", "Homepage")
val HomePageJS = Homepage.toJsComponent.raw
