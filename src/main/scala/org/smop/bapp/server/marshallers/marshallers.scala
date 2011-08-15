package org.smop.bapp.server.marshallers

import cc.spray.http.{ContentTypeRange, HttpContent}
import cc.spray.http.MediaTypes._
import cc.spray.marshalling.DefaultUnmarshallers.StringUnmarshaller
import cc.spray.marshalling.UnmarshallerBase
import com.codecommit.antixml.{XML, Elem}

object Marshallers {

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

}
