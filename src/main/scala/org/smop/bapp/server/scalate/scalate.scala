package org.smop.bapp.server.scalate

import org.fusesource.scalate._
import com.mongodb.casbah.gridfs.Imports._
import com.mongodb.casbah.MongoConnection
import util.{Resource, ResourceLoader}
import java.io.{StringWriter, PrintWriter, InputStream}

class MongoResource(gFile: GridFSDBFile) extends Resource {
  def uri: String = gFile.filename
  def inputStream: InputStream = gFile.inputStream
  def lastModified: Long = 0L
}

class MongoResourceLoader(gridfs: GridFS = GridFS(MongoConnection()("bapp"))) extends ResourceLoader {
  def resource(uri: String): Option[Resource] = gridfs.findOne(uri).map(gFile => new MongoResource(gFile))
}

object Scalate {
  lazy val engine = {
    val engine = new TemplateEngine()
    engine.resourceLoader = new MongoResourceLoader()
    engine
  }
  def render(uri: String, data: Map[String,Any]) = engine.layout(uri, data)
}
