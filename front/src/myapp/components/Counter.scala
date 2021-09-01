package myapp.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import myapp.Config
import org.scalajs.dom.document

val Counter = ScalaFnComponent
  .withHooks[Unit]
  .useState(0)
  .useEffectBy((_, count) =>
    Callback {
      document.title =
        if (count.value == 0) Config.title
        else s"You clicked ${count.value} times"
    }
  )
  .render((_, count) =>
    <.div(
      ^.className := "flex items-center justify-between",
      <.p(s"You clicked ${count.value} times"),
      <.button(
        ^.className := "bg-blue-600 hover:bg-blue-700 text-white text-sm px-4 py-2 border rounded-full",
        ^.onClick --> count.modState(_ + 1),
        "Click me"
      )
    )
  )
