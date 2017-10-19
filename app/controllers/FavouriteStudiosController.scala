package controllers

import models._
import play.api.cache.{Cache, Cached}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.Play.current

/**
  * Created by wilson on 10/19/17.
  */
class FavouriteStudiosController extends Controller {
  private def clearCaches(userId: Int, studioId: Int) =
    List(
      s"find_${userId}_${studioId}",
      s"findAll_${userId}"
    ).map { key =>
      Cache.remove(key)
    }

  def add(userId: Int, studioId: Int) = Action {
    val favourite = FavouriteStudio.addFavourite(userId, studioId)

    clearCaches(userId, studioId)

    Ok(Json.obj("result" -> favourite))
  }

  def remove(userId: Int, studioId: Int) = Action {
    val favourite = FavouriteStudio.delete(userId, studioId)

    clearCaches(userId, studioId)

    Ok(Json.obj("result" -> favourite))
  }

  def find(userId: Int, studioId: Int) = Cached(s"find_${userId}_${studioId}") {
    Action {
      val favouriteOpt = FavouriteStudio.find(userId, studioId)

      favouriteOpt match {
        case Some(favourite) => Ok(Json.obj("result" -> favourite))
        case None => NotFound(Json.obj("error" -> "NOT FOUND"))
      }
    }
  }

  def findAll(userId: Int) = Cached(s"findAll_${userId}") {
    Action {
      val allFavourites = FavouriteStudio.findAllByUser(userId)
      Ok(Json.obj("result" -> allFavourites))
    }
  }
}
