
Demo: type "sbt", inside sbt "console", inside the console the following (comments show expected output):


val e = Entry("id", "title", content="optional content via implicit")

// e: org.smop.bapp.model.Entry = Entry(id,Text(title),None,Some(TextContent(optional content via implicit)),None,None,None,None,None,2011-08-15T09:09:35.140+02:00)

// Don't leave off the type annotation, otherwise jodatime won't serialize
val eobj: MongoDBObject = grater[Entry].asDBObject(e)

// eobj: com.mongodb.casbah.Imports.MongoDBObject = Map(_typeHint -> org.smop.bapp.model.Entry, id -> id, title -> { "_typeHint" : "org.smop.bapp.model.Text" , "text" : "title"}, content -> { "_typeHint" : "org.smop.bapp.model.TextContent" , "text" : "optional content via implicit"}, updated -> 2011-08-15T09:09:35.140+02:00)

val newe = grater[Entry].asObject(eobj)

// newe: org.smop.bapp.model.Entry = Entry(id,Text(title),None,Some(TextContent(optional content via implicit)),None,None,None,None,None,2011-08-15T09:09:35.140+02:00)

toXML(e)

// res0: com.codecommit.antixml.Elem = <entry xmlns="http://www.w3.org/2005/Atom"><id>id</id><title type="text">title</title><updated>2011-08-15T09:09:35.140+02:00</updated></entry>
