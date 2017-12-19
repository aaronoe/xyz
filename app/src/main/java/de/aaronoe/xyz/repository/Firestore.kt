package de.aaronoe.xyz.repository

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.xyzApp
import id.zelory.compressor.Compressor
import java.io.File

object Firestore {

    private val USERS = "USERS"
    private val USER_FEED = "USER_FEED"
    private val USER_POSTS = "USER_POSTS"
    private val USER_FOLLOWING = "USER_FOLLOWING"

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

    fun makePostForUser(post : Post) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
                .set(post)

        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_FEED)
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

    fun getUserFollowingReference(user : User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_FOLLOWING)
    }

    fun createNewPost(user : User, description : String, localFile : File) {
        val post = Post(user, "", description)
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("images/${post.id}/${post.timestamp}.jpg")

        val compressedImageFile = Compressor(xyzApp.instance).compressToFile(localFile)

        fileRef.putFile(Uri.fromFile(compressedImageFile))
                .addOnSuccessListener {
                    post.mediaUrl = it.downloadUrl.toString()
                    makePostForUser(post)
                }
    }

    fun getCommentsReference(post : Post): CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
    }

    fun getPostReference(post: Post): DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
    }

}