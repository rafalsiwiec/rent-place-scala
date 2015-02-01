package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import play.api.Play.current
import play.api.libs.concurrent.Akka

import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._


object Chat extends Controller {
  implicit val timeout = Timeout(1 seconds)
  val room = Akka.system.actorOf(Props[ChatRoom])

  def showRoom(nick: String) = Action { implicit request =>
    Ok(views.html.chat.showRoom(nick))
  }

  def chatSocket(nick: String) = WebSocket.async { request =>
    println(s"Create chat socket for $nick")
    val channelsFuture = room ? Join(nick)
    channelsFuture.mapTo[(Iteratee[String, _], Enumerator[String])]
  }
}


case class Join(nick: String)
case class Leave(nick: String)
case class Broadcast(message: String)


class ChatRoom extends Actor {
  import context.dispatcher

  var users = Set[String]()
  val (enumerator, channel) = Concurrent.broadcast[String]

  def receive = {
    case Join(nick) => {
      if(!users.contains(nick)) {
        val iteratee = Iteratee.foreach[String]{ message =>
          self ! Broadcast("%s: %s" format (nick, message))
        }.map { _ => self ! Leave(nick) }

        users += nick
        println(s"User joined $nick")
        channel.push("User %s has joined the room, now %s users"
          format(nick, users.size))
        sender ! (iteratee, enumerator)
      } else {
        println(s"User already !! joined $nick")
        val enumerator = Enumerator("Nickname %s is already in use."
          format nick)
        val iteratee = Iteratee.ignore
        sender ! (iteratee, enumerator)
      }
    }
    case Leave(nick) => {
      users -= nick
      println(s"User left $nick")
      channel.push("User %s has left the room, %s users left"
        format(nick, users.size))
    }
    case Broadcast(msg: String) => channel.push(msg)
  }
}
