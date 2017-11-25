package de.aaronoe.xyz.repository

import com.google.firebase.auth.FirebaseAuth
import de.aaronoe.xyz.model.User

object AccountManager {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    var user : User? = null

    init {
        subscribeToUserUpdates()
    }

    fun isUserSet() = firebaseUser != null

    fun updateUser() {
        firebaseUser = firebaseAuth.currentUser
        subscribeToUserUpdates()
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

}