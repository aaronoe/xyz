package de.aaronoe.xyz.repository

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User

object Firestore {

    private val USERS = "USERS"
    private val USER_FEED = "USER_FEED"
    private val USER_POSTS = "USER_POSTS"

    fun getUsersReference() : CollectionReference =
            FirebaseFirestore
                    .getInstance()
                    .collection(USERS)

    fun saveUserAccount(user: User, completedListener: OnCompleteListener<Void>, failureListener: OnFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .set(user)
                .addOnCompleteListener(completedListener)
                .addOnFailureListener(failureListener)
    }

    fun getFeedPosts() : CollectionReference? {
        return AccountManager.user?.userId?.let {
            FirebaseFirestore.getInstance().collection(USERS).document(it).collection(USER_FEED)
        }
    }

    fun makeTestFeedPost(post : Post) {
        AccountManager.user?.userId?.let {
            FirebaseFirestore.getInstance()
                    .collection(USERS)
                    .document(it)
                    .collection(USER_FEED)
                    .document(post.id)
                    .set(post)
        }
    }

    fun makeUserTestPost(post : Post) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
                .set(post)

        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .update("postCount", post.author.postCount + 1)
    }

    fun getPostsForUser(user : User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_POSTS)
    }

    fun getUserReference(firebaseUser : FirebaseUser) : DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(firebaseUser.uid)
    }

    fun getUserReference(user : User) : DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
    }

}