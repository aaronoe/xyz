package de.aaronoe.xyz.messaging

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.ui.navigation.NavigationActivity
import de.aaronoe.xyz.ui.postdetail.PostDetailActivity
import de.aaronoe.xyz.ui.userdetail.UserActivity
import io.reactivex.Completable

object NotificationFactory {

    private const val DEFAULT_NOTIFICATION_CHANNEL = "DEFAULT_NOTIFICATION_CHANNEL"


    fun sendNewFollowerNotification(context: Context, newFollower: User): Completable {
        val mainActivtiyIntent = Intent(context, NavigationActivity::class.java)
        val userActivityIntent = UserActivity.getIntent(context, newFollower)

        val pendingIntent =
                TaskStackBuilder.create(context)
                        .addNextIntent(mainActivtiyIntent)
                        .addNextIntent(userActivityIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        return sendNotification(
                context,
                "You have a new follower!",
                "${newFollower.userName} is now following you",
                pendingIntent
        )
    }

    fun sendNewLikeNotification(context: Context, post: Post, likedUser: User): Completable {
        val mainActivtiyIntent = Intent(context, NavigationActivity::class.java)
        val postActivityIntent = PostDetailActivity.getIntent(context, post)

        val pendingIntent =
                TaskStackBuilder.create(context)
                        .addNextIntent(mainActivtiyIntent)
                        .addNextIntent(postActivityIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)

        return sendNotification(
                context,
                "Someone liked your post!",
                "${likedUser.userName} liked your post",
                pendingIntent
        )
    }

    private fun sendNotification(context: Context, title: String, contentText: String, pendingIntent: PendingIntent?): Completable {
        return Completable.create { emitter ->
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_action_clear_black)
                    //.setLargeIcon(getCircleBitmap(getBitmapFromURL(commentAuthorProfile)))
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    //.addAction(getReplyAction(group, post))
                    .setContentIntent(pendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.notify(0 /* ID of notification */, notificationBuilder.build())
                    ?: emitter.onError(ClassNotFoundException("NotificationManager does not exist"))
            emitter.onComplete()
        }
    }

}