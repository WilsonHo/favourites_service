package models

import models.DAO.FavouriteStudioDAO
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
  * Created by wilson on 10/19/17.
  */
case class FavouriteStudio(userId: Int, studioId: Int)

object FavouriteStudio {

  implicit val favouriteStudioFormat: Format[FavouriteStudio] = (
    (__ \ "user_id").format[Int] and
      (__ \ "studio_id").format[Int]
    ) (FavouriteStudio.apply, unlift(FavouriteStudio.unapply))

  def addFavourite(userId: Int, studioId: Int): FavouriteStudio = {
    val favourite = FavouriteStudio(userId, studioId)
    FavouriteStudioDAO.create(favourite)
    favourite
  }

  def delete(userId: Int, studioId: Int) = {
    val favourite = FavouriteStudio(userId, studioId)
    FavouriteStudioDAO.delete(favourite)
  }

  def find(userId: Int, studioId: Int): Option[FavouriteStudio] = {
    val favourite = FavouriteStudio(userId, studioId)
    if (FavouriteStudioDAO.exists(favourite)) {
      Some(favourite)
    } else {
      None
    }
  }

  def findAllByUser(userId: Int):List[FavouriteStudio] = {
    FavouriteStudioDAO.index(userId)
  }

}
