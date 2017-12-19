package de.aaronoe.xyz.repository

import android.net.Uri
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.aaronoe.rxfirestore.getCompletable
import de.aaronoe.rxfirestore.getObservable
import de.aaronoe.rxfirestore.setDocument
import de.aaronoe.rxfirestore.toSingle
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.xyzApp
import id.zelory.compressor.Compressor
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File


object XyzRepository {

    private const val TIMESTAMP = "timestamp"
    private const val MAX_UPLOAD_RETRY_TIME_MILLIS = 6000L

    fun getCreateUserAccountCompletable(user : User) : Completable {
        return Firestore
                .getUserReference(user)
                .setDocument(user)
    }

    fun getUserFeedObservable(user: User) : Observable<List<Post>> {
        return Firestore
                .getFeedPosts(user)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .getObservable()
    }

    fun createNewPost(user : User, description : String, localFile : File): Completable {
        val post = Post(user, "", description)
        val fileRef : StorageReference = FirebaseStorage.getInstance().run {
            maxUploadRetryTimeMillis = MAX_UPLOAD_RETRY_TIME_MILLIS
            reference.child("images/${post.id}/${post.timestamp}.jpg")
        }

        return Compressor(xyzApp.instance).compressToFileAsFlowable(localFile).singleOrError()
                .flatMap {
                    fileRef.putFile(Uri.fromFile(it)).toSingle()
                }
                .flatMapCompletable {
                    post.mediaUrl = it.downloadUrl.toString()
                    Completable.mergeArray(
                            Firestore.getPostReference(post).setDocument(post),
                            Firestore.getUserFeedPostReference(post).setDocument(post),
                            Firestore.getUserReference(post.author).update("postCount", post.author.postCount + 1).getCompletable())
                    }
    }

}