package models

case class Flat(
  flatId: Long,
  userId: Long,
  location: String,
  price: Int,
  size: Int,
  description: String)

object Flat {
  var flats = Set(Flat(5010255079763L, 1, "Raciborska 12/3", 4500, 90, "Large Plain Pack of 1000"),
                    Flat(5018206244666L, 1, "Grzegorzecka 51A", 3000, 70, "Giant Plain 51mm 100 pack"),
                    Flat(5018306332812L, 2, "Rakowicka 7/3", 800, 20, "Giant Plain Pack of 10000"),
                    Flat(5018306312913L, 2, "Lubicz 12B/1", 1200, 35, "No Tear Extra Large Pack of 1000"),
                    Flat(5018206244611L, 5, "Wesoła 4/15", 1000, 40, "Zebra Length 28mm Assorted 150 Pack"))

  def findAll = this.flats.toList.sortBy(_.location)

  def findByFlatId(id: Long) = this.flats.find(_.flatId == id)

  def save(flat: Flat) = {
    findByFlatId(flat.flatId).map( oldFlat =>
      this.flats = this.flats - oldFlat + flat
    ).getOrElse(throw new IllegalArgumentException("Flat not found"))
  }

  // Alternative if you want to use default serializer
  //import play.api.libs.json._
  //import play.api.libs.functional.syntax._
  //implicit val flatWrites = Json.writes[Flat]

  import play.api.libs.json._
  implicit object FlatWrites extends Writes[Flat] {
    def writes(f: Flat) = Json.obj(
      "flatId" -> Json.toJson(f.flatId),
      "userId" -> Json.toJson(f.userId),
      "location" -> Json.toJson(f.location),
      "price" -> Json.toJson(f.price),
      "size" -> Json.toJson(f.size),
      "description" -> Json.toJson(f.description)
    )
  }

  import play.api.libs.functional.syntax._
  implicit val flatReads: Reads[Flat] = (
    (JsPath \ "flatId").read[Long] and
    (JsPath \ "userId").read[Long] and
    (JsPath \ "location").read[String] and
    (JsPath \ "price").read[Int] and
    (JsPath \ "size").read[Int] and
    (JsPath \ "description").read[String]
  )(Flat.apply _)
}

