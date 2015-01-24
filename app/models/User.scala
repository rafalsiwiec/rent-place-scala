package models

import scala.util.Random


case class User(
  id: Long,
  username: String,
  name: Option[String],
  lastName: Option[String],
  email: String,
  phone: Option[String],
  activated: Boolean,
  confirmed: Boolean,
  password: String
)

case class ActivationSecret(
  userId: Long,
  secret: String
)

object User {
  import play.api.Logger
  import play.api.Play.current
  import play.api.db.DB

  import anorm.SQL
  import anorm.Row

  import play.api.data._
  import play.api.data.Forms._


  val random = new Random()

  private val sqlAllUsers = SQL("select * from users")
  private val sqlAddUser = SQL("insert into users VALUES({id}, {username}, {name}, {lastName}, {email}, {phone}, {activated}, {confirmed}, {password})")
  private val sqlUserById = SQL("select * from users where id = {id}")
  private val sqlActivateUser = SQL("update users set activated=true where id = {id}")
  private val sqlConfirmUser = SQL("update users set confirmed=true where id = {id}")
  private val sqlDeleteUser = SQL("delete from users where id = {id}")

  private val sqlSetSecret = SQL("insert into activations VALUES({userId}, {secret})")
  private val sqlGetSecret = SQL("select secret from activations where userId = {userId}")
  private val sqlDeleteSecret = SQL("delete from activations where userId = {userId}")

  val userForm = Form[User](
      mapping(
        "username" -> nonEmptyText,
        "name" -> optional(text),
        "lastName" -> optional(text),
        "email" -> nonEmptyText,
        "phone" -> optional(text),
        "password" -> nonEmptyText)
      ((username, name, lastName, email, phone, password) => User(random.nextLong, username, name, lastName, email, phone, false, false, password))
      ((user: User) => Some(user.username, user.name, user.lastName, user.email, user.phone, user.password))
    )

  val activateForm = Form[String](
    mapping("secret" -> text)
    (secret => secret)
    (secret => Some(secret))
  )

  private def mapRowToUser(row: Row): User = User(
    row[Long]("id"), row[String]("username"), row[Option[String]]("name"), row[Option[String]]("lastName"), row[String]("email"), row[Option[String]]("phone"),
    row[Boolean]("activated"), row[Boolean]("confirmed"), row[String]("password")
  )

  def getAllUsers(): List[User] = DB.withConnection { implicit connection => 
    sqlAllUsers().map( mapRowToUser _).toList
  }

  def getUserById(id: Long): Option[User] = DB.withConnection { implicit connection =>
    sqlUserById.on("id" -> id)().map( mapRowToUser _).headOption
  }

  def addUser(user: User) = DB.withConnection { implicit connection => 
    Logger.debug(s"Adding user $user")
    sqlAddUser
      .on("id" -> user.id,
          "username" -> user.username,
          "name" -> user.name,
          "lastName" -> user.lastName,
          "email" -> user.email,
          "phone" -> user.phone,
          "activated" -> user.activated,
          "confirmed" -> user.confirmed,
          "password" -> user.password)
      .executeInsert()
  }

  def deleteUser(userId: Long) = DB.withConnection { implicit connection =>
    Logger.debug(s"Deleting user $userId")
    sqlDeleteUser.on("id" -> userId).executeUpdate()
  }

  def confirmUser(userId: Long) = DB.withConnection { implicit connection =>
    Logger.debug(s"Confirming user $userId")
    sqlConfirmUser.on("id" -> userId).executeUpdate()
  }

  def generateActivationSecret(userId: Long): String = DB.withConnection { implicit connection =>
    val secret: String = random.alphanumeric.take(10).toList.mkString
    Logger.debug(s"Generated secret $secret for user $userId to activate")
    sqlDeleteSecret.on("userId" -> userId).executeUpdate()
    sqlSetSecret.on("userId" -> userId, "secret" -> secret).executeInsert()
    secret
  }

  def activateUser(userId: Long, secret: String): Boolean = DB.withConnection { implicit connection =>
    val userSecret = sqlGetSecret.on("userId" -> userId)().map( row => row[String]("secret") ).headOption.getOrElse("")
    if (secret == userSecret) {
      sqlDeleteSecret.on("userId" -> userId).executeUpdate()
      sqlActivateUser.on("id" -> userId).executeUpdate()
      Logger.debug(s"Activated user $userId")
      true
    } else {
      false
    }
  }
}
