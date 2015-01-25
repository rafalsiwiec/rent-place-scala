package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.libs.json.Json

import models.Flat

object FlatController extends Controller {

  def index = Action {
    Ok(views.html.flats.index())
  }

  def details(flatId: Long) = Action {
    Flat.findByFlatId(flatId) map { flat: Flat =>
      Ok(Json.toJson(flat))
    } getOrElse(NotFound)
  }

  def save(flatId: Long) = Action(parse.json) { request =>
    val flatJson = request.body
    val flat = flatJson.as[Flat]
    try {
      Flat.save(flat)
      Ok("Saved")
    }
    catch {
      case e:IllegalArgumentException =>
        BadRequest("Flat not found")
    }
  }

  def list = Action {
    val flats = Flat.findAll.map(_.flatId)
    Ok(Json.toJson(flats))
  }
}
