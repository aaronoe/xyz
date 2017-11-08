package de.aaronoe.xyz.repository

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import de.aaronoe.xyz.model.User

object Firestore {

    private val USERS = "USERS"
    private val USER_FEED = "USER_FEED"

    fun getUsersReference() : CollectionReference {
        return FirebaseFirestore.getInstance().collection(USERS)
    }

    fun saveUserAccount(user: User, completedListener: OnCompleteListener<Void>, failureListener: OnFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .set(user)
                .addOnCompleteListener(completedListener)
                .addOnFailureListener(failureListener)
    }

    fun getFeedPosts() : CollectionReference? {
        return AccountManager.user?.userId?.let { FirebaseFirestore.getInstance().collection(USERS).document(it).collection(USER_FEED) }
    }

}