package myapp

object Config:
  val title = "My Scala Startup"
  val description = List(
    """
    |This website is a next.js application, implemented in Scala 3, 
    |along with a purely-functional server running in the JVM!
    """.stripMargin,
    "This text is shared between the React app and the server",
    "Enjoy that type-safe goodness!"
  )
