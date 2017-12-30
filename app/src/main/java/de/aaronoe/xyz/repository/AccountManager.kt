package de.aaronoe.xyz.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import de.aaronoe.rxfirestore.getObservable
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.messaging.XyzInstanceIdService
import de.aaronoe.xyz.model.User

object AccountManager {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val followingLiveData = MutableLiveData<List<User>>()
    private var firebaseUser = firebaseAuth.currentUser
    var user : User? = firebaseUser?.let { User(it) }

    init {
        updateUser()
    }

    fun isUserSet() = firebaseUser != null

    fun updateUser() {
        firebaseUser = firebaseAuth.currentUser
        user = firebaseUser?.let { User(it) }
        subscribeToUserUpdates()
        subscribeToUserFollowingUpdates()
    }

    fun onFirstLogin() {
        updateUser()
        XyzInstanceIdService.uploadNewToken()
    }

    private fun subscribeToUserUpdates() {
        firebaseUser?.let {
            Firestore.getUserReference(it)
                    .getObservable<User>()
                    .subscribeDefault { user = it }
        }
    }

    private fun subscribeToUserFollowingUpdates() {
        firebaseUser?.let {
            Firestore.getUserFollowingReference(User(it))
                    .getObservable<User>()
                    .subscribeDefault { followingLiveData.value = it }
        }
    }

    fun isFollowedByUser(user : User) : Boolean {
        if (this.user == null) return false
        followingLiveData.value.orEmpty().apply {
            this.find { it.userId == user.userId }?.let {
                return true
            }
        }
        return false
    }

}