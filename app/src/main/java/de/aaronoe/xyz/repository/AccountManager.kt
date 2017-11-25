package de.aaronoe.xyz.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import de.aaronoe.xyz.model.User

object AccountManager {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private val followingLiveData = MutableLiveData<List<User>>()
    var user : User? = null

    init {
        subscribeToUserUpdates()
        subscribeToUserFollowingUpdates()
    }

    fun isUserSet() = firebaseUser != null

    fun updateUser() {
        firebaseUser = firebaseAuth.currentUser
        subscribeToUserUpdates()
        subscribeToUserFollowingUpdates()
    }

    private fun subscribeToUserUpdates() {
        firebaseUser?.let {
            Firestore.getUserReference(it)
                    .addSnapshotListener { documentSnapshot, _ ->
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User::class.java)
                        }
                    }
        }
    }

    private fun subscribeToUserFollowingUpdates() {
        firebaseUser?.let {
            Firestore.getUserFollowingReference(User(it))
                    .addSnapshotListener { querySnapshot, _ ->
                        querySnapshot?.let {
                            val postList  = mutableListOf<User>()
                            it.forEach {
                                if (it.exists()) {
                                    postList.add(it.toObject(User::class.java))
                                }
                            }
                            followingLiveData.value = postList
                        }
                    }
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