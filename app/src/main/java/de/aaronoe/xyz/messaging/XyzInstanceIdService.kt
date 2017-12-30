package de.aaronoe.xyz.messaging

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.XyzRepository


class XyzInstanceIdService : FirebaseInstanceIdService() {

    companion object {
        const val TAG = "XyZInstanceIdService"

        fun uploadNewToken() {
            val refreshedToken = FirebaseInstanceId.getInstance().token ?: return
            AccountManager.user?.let {
                XyzRepository.uploadUserMessagingToken(it, refreshedToken)
                        .subscribeDefault(onSuccess = {
                            Log.e(TAG, "Success uploading token: $refreshedToken")
                        }, onError = {
                            Log.e(TAG, "Error uploading token: $refreshedToken")
                        })
            }
        }
    }

    override fun onTokenRefresh() {
        uploadNewToken()
    }

}