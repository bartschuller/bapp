package org.smop.bapp.mongodb

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.Imports._
import org.smop.bapp.model.Entry

object EntryDAO extends SalatDAO[Entry, String](collection = MongoConnection()("bapp")("entry")) {
  com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()
  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()
}
