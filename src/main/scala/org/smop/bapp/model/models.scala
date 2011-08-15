package org.smop.bapp.model

object ModelTypes {
  type URI = String
  type IRI = String
}

import ModelTypes._
import com.novus.salat.annotations.raw.Salat
import org.scala_tools.time.Imports._
import com.codecommit.antixml.Elem
import com.novus.salat.annotations.raw.Key

@Salat sealed trait TextConstruct
case class Text(text: String) extends TextConstruct
case class Html(html: String) extends TextConstruct
case class Xhtml(xhtml: String) extends TextConstruct

case class Person(name: String, email: Option[String] = None, uri: Option[URI] = None)

@Salat sealed trait Content
case class TextContent(text: String) extends Content
case class TextBasedContent(contentType: String, text: String) extends Content
case class HtmlContent(html: String) extends Content
case class XhtmlContent(xhtml: String) extends Content
case class XMLContent(contentType: String, xml: String) extends Content
case class OutOfLineContent(contentType: String, src: URI) extends Content

case class Category(term: String, scheme: Option[URI]=None, label: Option[String] = None)

case class Link(href: URI, rel: Option[String]=None, mediaType: Option[String]=None, hrefLang: Option[String]=None, title: Option[String]=None, length: Option[String]=None)

case class Entry(@Key("_id") id: IRI, title: TextConstruct, summary: Option[TextConstruct] = None, content: Option[Content]=None, author: Option[List[Person]]=None, contributor: Option[List[Person]]=None, category: Option[List[Category]] = None,
  link: Option[List[Link]]=None, published: Option[DateTime]=None, updated: DateTime = DateTime.now)

case class Feed(author: List[Person])

object Implicits {
  implicit def string2Text(s:String) = Text(s)
  implicit def string2SomeTextContent(s:String) = Some(TextContent(s))
}
