package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import models.User


object UserController extends Controller {

  def listUsers = Action { implicit request =>
    Ok(views.html.users.userList(User.getAllUsers))
  }

  def userDetails(id: Long) = Action {
    User.getUserById(id) match {
      case Some(user) => Ok(views.html.users.userDetail(user, User.activateForm))
      case None => NotFound(s"User not found $id")
    }
  }

  def addUser = Action {
    Ok(views.html.users.addUser(User.userForm))
  }

  def addUserSubmit = Action { implicit request =>
    User.userForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.users.addUser(formWithErrors)),
      user => {
        User.addUser(user)
        Redirect(routes.UserController.listUsers)
      }
    )
  }

  def deleteUser(userId: Long) = Action {
    User.deleteUser(userId)
    Redirect(routes.UserController.listUsers).flashing("message" -> s"User $userId deleted ;(")
  }

  def confirmUser(userId: Long) = Action {
    User.confirmUser(userId)
    Redirect(routes.UserController.listUsers).flashing("message" -> s"User $userId confirmed :)")
  }

  def activateUser(userId: Long) = Action { implicit request =>
    User.getUserById(userId) match {
      case Some(user) => {
        User.activateForm.bindFromRequest.fold(
          formWithErrors => Ok(views.html.users.userDetail(user, formWithErrors)),
          secret => {
            if (User.activateUser(userId, secret)) {
              Redirect(routes.UserController.userDetails(userId)).flashing("message" -> s"User $userId activated succesfully :)")
            } else {
              Ok(views.html.users.userDetail(user, User.activateForm.withError("secret", "Wrong secret key!")))
            }
          }
        )
      }
      case None => NotFound(s"User not found $userId")
    }
  }

  def generateActivationSecret(userId: Long) = Action {
    User.getUserById(userId) match {
      case Some(user) => Ok(views.html.users.displayGeneratedSecret(user, User.generateActivationSecret(user.id)))
      case None => NotFound(s"User not found $userId")
    }
  }
}


