package de.aaronoe.xyz.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.repository.XyzRepository

class XyzMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "XyzMessagingService"

        private const val MESSAGING_KEY_EVENT = "event"

        private const val NEW_FOLLOWER_EVENT = "NEW_FOLLOWER_EVENT"
        private const val NEW_LIKE_EVENT = "NEW_LIKE_EVENT"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {

            val payload = message.data

            val event = payload[MESSAGING_KEY_EVENT] ?: return

            when(event) {
                NEW_FOLLOWER_EVENT -> onNewFollowerEvent(payload)
                NEW_LIKE_EVENT -> onNewLikeEvent(payload)
            }

        }
    }

    private fun onNewFollowerEvent(payload: MutableMap<String, String>) {
        val followerId = payload["followerId"] ?: return

        XyzRepository.getUserSingle(followerId)
                .flatMapCompletable { NotificationFactory.sendNewFollowerNotification(this.applicationContext, it) }
                .subscribeDefault(onComplete = {
                    Log.d(TAG, "New Follower notification send successfully")
                }, onError = {
                    Log.d(TAG, "Failed sending new follower notification")
                })
    }

    private fun onNewLikeEvent(payload: MutableMap<String, String>) {
        val likeUserId = payload["likeUserId"] ?: return
        val postId = payload["postId"] ?: return
        val postAuthorId = payload["postAuthorId"] ?: return

        XyzRepository.getUserPostPairSingle(postAuthorId, likeUserId, postId)
                .flatMapCompletable { NotificationFactory.sendNewLikeNotification(this.applicationContext, it.second, it.first) }
                .subscribeDefault(onComplete = {
                    Log.d(TAG, "New like notification send successfully")
                }, onError = {
                    Log.d(TAG, "Failed sending new like notification")
                })
    }

}