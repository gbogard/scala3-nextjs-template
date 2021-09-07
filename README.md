# Scala 3 / NextJS Starter

This is a Scala 3, Next.js and Tailwind template featuring:
- a [next.js](https://nextjs.org/) web application using Scala 3 and 
@japgolly's awesome [scalajs-react](https://github.com/japgolly/scalajs-react/)
- Full Tailwind v2 configuration with autoprefixer, purge-css and cssnano
- a `NextApp` sbt plugin that lets you add more Next.js sub-projects if you need them
- a `server` sbt project for your server-side logic, preconfigured with sbt-revolver
- a `shared` sbt project for sharing bits of code between your next.js app and your back end 
(awesome for sharing [Tapir](https://tapir.softwaremill.com/en/latest/) endpoints for example)

## Setup

### Install node dependencies:

```
cd front && npm install
```

Alternatively, you can run it directly from sbt, from the root repository:

```
sbt npmInstall
```

This is provided as a convenience by the `NextApp` plugin. 
However, running external commands from sbt appears to
be significantly slower than running them from a terminal, at least on my laptop :(

## Development

### Launch the Next.js dev server

```
cd front && npm run dev
```

Again, if you'd like to run everything from sbt, the `NextApp` plugin provides several sbt commands:
- `startNextServer` to start the dev server in the background. Does nothing if the server is already running.
- `stopNextServer` to stop the dev server
- `show nextServerIsRunning` to check if the dev server is running


### Build the Javascript from Scala.js (and watch for changes):

In another terminal:

```
sbt ~fastLinkJS
```

## Production build

### 1) Build the Javascript from Scala.js (with full optimization):

```
sbt fullLinkJS
```

### 2) Build the Next.js app

```
cd front && npm run build
```

Or, from sbt:

```
sbt nextBuild
```

---

## Q&A

### Why next.js?

Next.js does a lot of things right out-of-the-box, this means that most applications won't need
a custom and complex Webpack config. With this setup, you get these things for free:
- Fast Refresh
- Static generation and server-side rendering of pages
- Easy routing
- CSS modules (see an example of this in `Homepage`)

Sure, you could turn to a more elaborate setup, using GraalVM for SSR, if you needed to, and unlock more
freedom, but this setup should be enough for many use cases.

### How do I add a new page to my Next.js app?

You cannot write Scala code in the `pages/` folder. Scala sources are meant to go in the `src/` folder, and
Scala.js outputs ES6 modules in a single directory (`target/js`).

To add a new page, you need to create a scalajs-react component somewhere in your Scala code, export it, 
for example like this:

```scala
// Will export a "Component" value in the "@scalajs/Homepage" module
@JSExportTopLevel("Component", "Homepage")
val HomePageJS = Homepage.toJsComponent.raw
```

Then, you can create a `.js` file in `pages/` and re-export your Scala component:

```Javascript
export {Component as default} from '../target/js/Homepage';
```

Take a look at 
[scalajs-react's documentation on interoperability](https://github.com/japgolly/scalajs-react/blob/master/doc/INTEROP.md) 
for more info.
