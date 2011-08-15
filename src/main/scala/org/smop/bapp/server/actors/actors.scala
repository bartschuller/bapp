package org.smop.bapp.server.actors

import akka.actor.Actor
import org.smop.bapp.model.Entry
import org.smop.bapp.mongodb.EntryDAO

sealed trait DAOMessage
case class CreateEntry(entry: Entry) extends DAOMessage
case class RetrieveEntry(id: String) extends DAOMessage
case class ResultEntry(entry: Option[Entry]) extends DAOMessage

class MongoActor extends Actor {
  def receive = {
    case CreateEntry(e) => self reply ResultEntry(EntryDAO.insert(e).map(
      (nid:String) =>e.copy(id=
        nid.toString)))
  }
}
