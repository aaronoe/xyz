package de.aaronoe.xyz.repository

import com.google.firebase.auth.FirebaseAuth
import de.aaronoe.xyz.model.User

object AccountManager {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    var user = firebaseUser?.let { User(it) }

    fun isUserSet() = firebaseUser == null

    fun updateUser() {
        firebaseUser = firebaseAuth.currentUser
        user = firebaseUser?.let { User(it) }
    }

}