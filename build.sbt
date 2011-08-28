// Project specific settings.
name := "bapp"

organization := "org.smop"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.0-1"

/////////////////////////////////////////////////////////////////
// Spray
/////////////////////////////////////////////////////////////////
// Some of the transitive dependencies have invalid md5 hashes in their Maven repos, so this
// bypasses that problem. This should probably be removed in the future once all those things
// are brought up to date. Recommended by Mark Harrah.
checksums := Nil

// Absolutely necessary dependencies.
libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka" % "1.1.3",
  "cc.spray" %% "spray-http" % "0.7.0" % "compile" withSources(),
  "cc.spray" %% "spray-server" % "0.7.0" % "compile" withSources()
)

// Testing dependencies.
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.5" % "test",
  "org.eclipse.jetty" % "jetty-server" % "8.0.0.M3" % "jetty",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.0.M3" % "jetty"
)

// slf4j is not required but a good option for logging. logback is a good slf4j backend.
libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka-slf4j" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "0.9.29" % "runtime"
)

// Inject the xsbt-web-plugin webSettings into these project settings to enable
// commands such as jetty-run.
seq(webSettings :_*)

// When depending on a spray snapshot build we need the ScalaToolsSnapshots resolver
// GuiceyFruit is required because their things were removed from public Maven repos
// and some of their jars are transitive dependencies.
resolvers ++= Seq(
  ScalaToolsSnapshots,
  "GuiceyFruit Release Repository" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
)

resolvers += "Akka Repository" at "http://akka.io/repository"

//////////////////////////////////////////////////////////////////////
// Salat
//////////////////////////////////////////////////////////////////////
resolvers ++= Seq(
  "repo.novus rels" at "http://repo.novus.com/releases/",
  "repo.novus snaps" at "http://repo.novus.com/snapshots/")

libraryDependencies += "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"

/////////////////////////////////////////////////////////////////////
// MongoDB
/////////////////////////////////////////////////////////////////////
libraryDependencies += "com.mongodb.casbah" %% "casbah-gridfs" % "2.1.5.0"

/////////////////////////////////////////////////////////////////////
// Anti-XML
/////////////////////////////////////////////////////////////////////
libraryDependencies += "com.codecommit" %% "anti-xml" % "0.3-SNAPSHOT"

/////////////////////////////////////////////////////////////////////
// Coffeescript
// https://github.com/softprops/coffeescripted-sbt#readme
/////////////////////////////////////////////////////////////////////
seq(coffeescript.CoffeeScript.coffeeSettings: _*)

/////////////////////////////////////////////////////////////////////
// Scalate
/////////////////////////////////////////////////////////////////////
libraryDependencies += "org.fusesource.scalate" % "scalate-core" % "1.5.1"

/////////////////////////////////////////////////////////////////////
// Bapp
/////////////////////////////////////////////////////////////////////
// Weird error about slf4j-api 1.6.1 not resolvable. Explicit dep:
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.1"

initialCommands in console :=
"""
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import org.smop.bapp.model._
import org.smop.bapp.model.Implicits._
import org.smop.bapp.model.BappXMLFormat._
"""
