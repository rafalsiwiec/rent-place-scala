package models

import java.util.Date
import play.api.db.slick.Config.driver.simple._

case class Rent(
  id: Long,
  userId: Long,
  flatId: Long,
  from: Date,
  till: Option[Date],
  confirmed: Boolean)

class Rents(tag: Tag) extends Table[Rent](tag, "RENT") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("userId", O.NotNull)
  def flatId = column[Long]("flatId", O.NotNull)
  def from = column[Date]("from")
  def till = column[Date]("till", O.Nullable)
  def confirmed = column[Boolean]("confirmed")

  def * = (id, userId, flatId, from, till.?, confirmed) <> (Rent.tupled, Rent.unapply _)

  implicit val JavaUtilDateMapper =
    MappedColumnType .base[Date, java.sql.Timestamp] (
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))
}

object Rents {
  val rentQuery = TableQuery[Rents]

  def insert(rent: Rent)(implicit s: Session) {
    rentQuery.insert(rent)
  }

  def delete(id: Long)(implicit s: Session) {
    rentQuery.filter(_.id === id).delete
  }

  def list()(implicit s: Session): Seq[Rent] = {
    val result = for (
      rent <- rentQuery
    ) yield rent
    result.list
  }
}
