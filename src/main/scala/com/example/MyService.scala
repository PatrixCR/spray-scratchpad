package com.example

import akka.actor.Actor
import akka.event.Logging
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing._
import spray.http._
import MediaTypes._

import scala.util.Random

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with Json4sSupport {

  override implicit def json4sFormats: Formats = DefaultFormats

  val myRoute =
    logRequest("route", Logging.InfoLevel) {
      path("") {
        get {
          complete {
            s"${Random.nextInt(100)}"
          }
        }
      }
    }
}