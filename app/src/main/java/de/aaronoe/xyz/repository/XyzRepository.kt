package de.aaronoe.xyz.repository

import android.net.Uri
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.aaronoe.rxfirestore.*
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.xyzApp
import id.zelory.compressor.Compressor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.io.File


object XyzRepository {

    private const val TIMESTAMP = "timestamp"
    private const val MAX_UPLOAD_RETRY_TIME_MILLIS = 6000L

    fun getCreateUserAccountCompletable(user: User): Completable {
        return Firestore
                .getUserReference(user)
                .setDocument(user)
    }

    fun getUserFeedObservable(user: User): Observable<List<Post>> {
        return Firestore
                .getFeedPosts(user)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .getObservable()
    }

    fun getUserObservable(user: User) : Observable<User> {
        return Firestore.getUserReference(user).getObservable()
    }

    fun getPostObservable(post: Post): Observable<Post> {
        return Firestore.getPostReference(post).getObservable()
    }

    fun getCommentsForPostObservable(post : Post) : Observable<List<Comment>> {
        return Firestore.getCommentsReference(post)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .getObservable()
    }

    fun getPostsForUserObservable(user: User) : Observable<List<Post>> {
        return Firestore.getPostsForUser(user)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .getObservable()
    }

    fun createNewPost(user: User, description: String, localFile: File): Completable {
        val post = Post(user, "", description)
        val fileRef: StorageReference = FirebaseStorage.getInstance().run {
            maxUploadRetryTimeMillis = MAX_UPLOAD_RETRY_TIME_MILLIS
            reference.child("images/${post.id}/${post.timestamp}.jpg")
        }

        return Compressor(xyzApp.instance).compressToFileAsFlowable(localFile)
                .singleOrError()
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

    fun postNewComment(post: Post, comment: Comment) : Completable {
        return Firestore.getCommentReference(post, comment)
                .setDocument(comment)
    }

    fun searchUsers(query: String) : Single<List<User>> {
        return Firestore.getUsersReference()
                .whereGreaterThanOrEqualTo("queryName", query.toLowerCase())
                .limit(15).getSingle()
    }

    fun searchForUsers(query: String) : Single<List<User>> {
        return Single.zip(
                Firestore.getUsersReference().whereGreaterThanOrEqualTo("userName", query).limit(10).getSingle<User>(),
                Firestore.getUsersReference().whereLessThanOrEqualTo("userName", query).limit(10).getSingle<User>(),
                BiFunction { t1, t2 -> t1.union(t2).toList() }
        )
    }

}