package actors

import akka.actor.{Actor, Props}
import models.{FavouriteNotification, FavouriteStudio, Friend}
import scala.concurrent.ExecutionContext.Implicits.global


object NotificationActor {
  def props: Props = Props(new NotificationActor)
}

class NotificationActor extends Actor {
  def receive = {
    case favourite: FavouriteStudio =>
      notifyFriendsOfFavourite(favourite)
  }

  private def notifyFriendsOfFavourite(favourite: FavouriteStudio): Unit = {
    // Lookup friends
    val fFriends = Friend.findAllFriends(favourite.userId)

    // Send a push notification to each friend
    for (
      friends <- fFriends;
      friend <- friends;
      notification = FavouriteNotification(friend, favourite)
    ) {
      notification.send()
    }
  }

}
