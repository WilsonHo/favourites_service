package models.DAO

import anorm.SqlParser.scalar
import anorm._
import models.FavouriteStudio
import play.api.Play.current
import play.api.db.DB

object FavouriteStudioDAO {
  def create(favourite: FavouriteStudio) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |INSERT IGNORE INTO `favourite_studio` (`user_id`, `studio_id`)
          |VALUES ({userId}, {studioId});
        """.stripMargin)
        .on(
          "userId" -> favourite.userId,
          "studioId" -> favourite.studioId
        ).executeInsert()
    }

  }

  def delete(favourite: FavouriteStudio) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |DELETE FROM `favourite_studio`
          |WHERE `user_id`={userId} AND `studio_id`={studioId}
          |LIMIT 1;
        """.stripMargin)
        .on(
          "userId" -> favourite.userId,
          "studioId" -> favourite.studioId
        ).executeUpdate()
    }
  }

  def exists(favourite: FavouriteStudio): Boolean = {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          |SELECT COUNT(*) AS num_matches
          |FROM `favourite_studio`
          |WHERE `user_id`={userId} AND `studio_id`={studioId};
        """.stripMargin).on(
        "userId" -> favourite.userId,
        "studioId" -> favourite.studioId
      ).executeQuery()
        .as(parserInt)

      result != 0
    }
  }

  def index(userId: Int): List[FavouriteStudio] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          |SELECT user_id, studio_id
          |FROM `favourite_studio`
          |WHERE `user_id`={userId};
        """.stripMargin).on(
        "userId" -> userId
      ).executeQuery().as(parser *)
    }
  }

  val parser: RowParser[FavouriteStudio] = Macro.parser[FavouriteStudio]("user_id", "studio_id")
  val parserInt: ResultSetParser[Int] = scalar[Int].single

  //  /**
  //    * Parse a FavouriteStudio from a ResultSet
  //    */
  //  private[models] val simple = {
  //    get[Int]("favourite_studio.user_id") ~
  //      get[Int]("favourite_studio.studio_id") map {
  //      case user_id ~ studio_id => FavouriteStudio(user_id, studio_id)
  //    }
  //  }
}

//object FavouriteStudioDAO {

//

//

//

//

//}
