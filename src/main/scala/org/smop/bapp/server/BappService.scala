package org.smop.bapp.server

import actors.{ResultEntry, RetrieveEntry, CreateEntry}
import cc.spray._
import http.StatusCodes
import http.MediaTypes._
import com.codecommit.antixml.{Text=>_,_}
import marshallers.Marshallers._
import org.smop.bapp.model.BappXMLFormat._
import org.smop.bapp.model._
import akka.actor.ActorRef
import scalate.Scalate

trait BappService extends Directives {
  val dao: ActorRef
  val notFoundEntry = Entry("bapp:notfound", Text("Not Found"), content=Some(HtmlContent("Entry not found")))

  val bappService = {
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          _.complete {
            val homeEntry: Option[Entry] = (dao !! RetrieveEntry("bapp:home")).flatMap(_.asInstanceOf[ResultEntry].entry)
            Scalate.render("/templates/index.jade", Map("entry"->homeEntry.getOrElse(notFoundEntry)))
          }
        }
      }
    } ~
    path("js") {
      getFromResourceDirectory("/js")
    } ~
    path("api") {
      get {
        respondWithMediaType(CustomMediaType("application/atomsvc+xml")) {
          _.complete(serviceDoc)
        }
      }
    } ~
    path("blog/main") {
      post {
        content(as[Elem]) { xml =>
          val entry = fromXML[Entry](xml)
          println(entry)
          val dbentry = dao !! CreateEntry(entry)
          println(dbentry)
          respondWithStatus(StatusCodes.Created) {
            _.complete(toXML(entry).toString)
          }
        }
      }
    }
  }

  def serviceDoc =
<service xmlns="http://www.w3.org/2007/app"
         xmlns:atom="http://www.w3.org/2005/Atom">
  <workspace>
    <atom:title>Bapp</atom:title>
    <collection
        href="http://localhost:8080/blog/main" >
      <atom:title>My Blog Entries</atom:title>
    </collection>
  </workspace>
</service>
}
