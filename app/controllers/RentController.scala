package controllers

import play.api._
import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import models._

object RentController extends Controller {

  val form = Form[Rent](
    mapping(
      "id" -> of[Long],
      "userId" -> of[Long],
      "flatId" -> of[Long],
      "from" -> date,
      "till" -> optional(date),
      "confirmed" -> boolean)
  (Rent.apply)(Rent.unapply))

  def list = DBAction { implicit request =>
    Ok(views.html.rents.index(Rents.list(), form))
  }

  def insert = DBAction { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest,
      rent => {
        Rents.insert(rent)
        Redirect(routes.RentController.list)
      }
    )
  }

  def delete(id: Long) = DBAction { implicit request =>
    Rents.delete(id)
    Redirect(routes.RentController.list)
  }
}
