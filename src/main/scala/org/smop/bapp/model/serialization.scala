package org.smop.bapp.model

import com.codecommit.antixml
import antixml._
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime


// Design from http://debasishg.blogspot.com/2010/07/refactoring-into-scala-type-classes.html

trait FromXML[T] {
  def convertFromXML(xml:Elem): T
}

trait ToXML[T] {
  def convertToXML(t: T):Elem
}

trait AppendToXML[T] extends ToXML[T] {
  def appendToXML(t: T, parent: Elem):Elem
  override def convertToXML(t: T):Elem = appendToXML(t, <wrapper/>.convert)
}

trait Format[T] extends FromXML[T] with ToXML[T]


object BappXMLFormat {
  def fromXML[T](xml: Elem)(implicit format: Format[T]) = format.convertFromXML(xml)
  def toXML[T](t: T)(implicit format: Format[T]) = format.convertToXML(t)
  def toXML[T](t: T, parent: Elem)(implicit format: AppendToXML[T]) = format.appendToXML(t, parent)

  private val atomns = "http://www.w3.org/2005/Atom"
  private val nsmap = Map(""->atomns)
  private def atomelem(name: String, children: Node*) = Elem(None, name, Attributes.empty, nsmap,Group(children:_*))
  private val dtFmt = ISODateTimeFormat.dateTime
  implicit private def DateTime2String(dt: DateTime) = dt.toString(dtFmt)

  implicit object TextConstructFormat extends Format[TextConstruct] with AppendToXML[TextConstruct] {
    def convertFromXML(xml: Elem) = null

    def appendToXML(tc: TextConstruct, p: Elem): Elem = tc match {
      case Text(t) => p.copy(attrs=p.attrs + ("type"-> "text"), children = Group(antixml.Text(t)))
      case Html(h) => p.copy(attrs=p.attrs + ("type"-> "html"), children = Group(antixml.Text(h)))
      case Xhtml(x) => p.copy(attrs=p.attrs + ("type"-> "xhtml"), children = Group(XML.fromString(x)))
    }
  }

  implicit object ContentFormat extends Format[Content] with AppendToXML[Content] {
    def convertFromXML(xml: Elem) = null

    def appendToXML(c: Content, p: Elem): Elem = c match {
      case TextContent(t) => p.copy(attrs=p.attrs + ("type"-> "text"), children = Group(antixml.Text(t)))
      case HtmlContent(h) => p.copy(attrs=p.attrs + ("type"-> "html"), children = Group(antixml.Text(h)))
      case XhtmlContent(x) => p.copy(attrs=p.attrs + ("type"-> "xhtml"), children = Group(XML.fromString(x)))
    }
  }
  // case class Entry(@Key("_id") id: IRI, title: TextConstruct, summary: Option[TextConstruct] = None, content: Option[Content]=None, author: Option[List[Person]]=None, contributor: Option[List[Person]]=None, category: Option[List[Category]] = None,
//  link: Option[List[Link]]=None, published: Option[DateTime]=None, updated: DateTime = DateTime.now)
  
  implicit object EntryFormat extends Format[Entry] {
    def convertFromXML(xml: Elem) =
      Entry(
        id = xml \ "id" \ text head,
        title = (xml \ "title").map(t =>
          t.attrs("type") match {
            case "text" => Text(t \ text head)
            case "html" => Html(t \ text head)
            case "xhtml" => Xhtml(t \ * toString)
          }).head,
        content = (xml \ "content").map(c =>
          c.attrs("type") match {
            case "text" => TextContent(c \ text mkString)
            case "html" => HtmlContent(c \ text mkString)
            case "xhtml" => XhtmlContent(c \ * toString)
          }).headOption
      )

    def convertToXML(e: Entry) = {
      atomelem("entry",
        Seq(
          Some(atomelem("id", antixml.Text(e.id))),
          Some(toXML(e.title, <title/>.convert)),
          Some(atomelem("updated", antixml.Text(e.updated))),
          e.content.map(c => toXML(c, <content/>.convert))).flatten:_*
      )
    }
  }

}
