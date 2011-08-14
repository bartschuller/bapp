package org.smop.bapp.server

import cc.spray._
import http.{HttpContent, ContentTypeRange, StatusCodes}
import http.MediaTypes._
import marshalling.UnmarshallerBase
import com.codecommit.antixml._

trait BappService extends Directives {
  implicit object AntiXMLUnmarshaller extends UnmarshallerBase[Elem] {
    val canUnmarshalFrom = ContentTypeRange(`text/xml`) ::
                           ContentTypeRange(`text/html`) ::
                           ContentTypeRange(`application/xhtml+xml`) ::
                           ContentTypeRange(`application/atom+xml`) :: Nil

    def unmarshal(content: HttpContent) = protect {
      if (content.contentType.charset.isDefined) {
        XML.fromString(StringUnmarshaller.unmarshal(content).right.get)
      } else {
        XML.fromInputStream(content.inputStream)
      }
    }
  }

  val bappService = {
    path("") {
      get {
        _.complete("Here you are")
      }
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
          println(xml)
          respondWithStatus(StatusCodes.Created) {
            _.complete(xml.toString)
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
