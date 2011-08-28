package org.smop.bapp.server

import org.specs2.mutable._
import cc.spray._
import test._
import http._
import HttpMethods._
import StatusCodes._

class BappServiceSpec extends Specification with SprayTest with BappService {
  "The HelloService" should {
    "return a greeting for GET requests to the root path" in {
      testService(HttpRequest(GET, "/")) {
        bappService
      }.response.content.as[String] mustEqual Right("Say hello to Spray!")
    }
    "leave GET requests to other paths unhandled" in {
      testService(HttpRequest(GET, "/kermit")) {
        bappService
      }.handled must beFalse
    }
    "return a MethodNotAllowed error for POST requests to the root path" in {
      testService(HttpRequest(POST, "/")) {
        bappService
      }.response mustEqual HttpResponse(MethodNotAllowed, "HTTP method not allowed, supported methods: GET")
    }
  }
  val dao = null
}
