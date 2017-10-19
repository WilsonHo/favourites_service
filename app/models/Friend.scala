package models

import models.DAO.FriendDAO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

case class Friend(friendId: Int)

object Friend {
  def findAllFriends(userId: Int)(implicit ec: ExecutionContext): Future[Set[Friend]] =
    FriendDAO.index(userId).map(_.toSet)
}

