package de.aaronoe.xyz.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User

object Firestore {

    private const val USERS = "USERS"
    private const val USER_FEED = "USER_FEED"
    private const val USER_POSTS = "USER_POSTS"
    private const val USER_FOLLOWING = "USER_FOLLOWING"
    private const val USER_FOLLOWERS = "USER_FOLLOWERS"
    private const val POST_COMMENTS = "COMMENTS"
    private const val POST_LIKES = "POST_LIKES"
    private const val USER_MESSAGING_TOKENS = "USER_MESSAGING_TOKENS"

    fun getFeedPosts(user: User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_FEED)
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

    fun getUserReference(userId : String) : DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(userId)
    }

    fun getUsersReference() : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
    }

    fun getUserFollowingReference(user : User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_FOLLOWING)
    }

    fun getUserFollowersReference(user : User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_FOLLOWERS)
    }

    fun getCommentsReference(post : Post): CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
                .collection(POST_COMMENTS)
    }

    fun getPostReference(post: Post): DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
    }

    fun getPostReference(postId: String, authorId: String): DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(authorId)
                .collection(USER_POSTS)
                .document(postId)
    }

    fun getUserFeedPostReference(post: Post) : DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_FEED)
                .document(post.id)
    }

    fun getCommentReference(post: Post, comment: Comment) : DocumentReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(post.author.userId)
                .collection(USER_POSTS)
                .document(post.id)
                .collection(POST_COMMENTS)
                .document(comment.id)
    }

    fun getUserMessagingTokensReference(user : User) : CollectionReference {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.userId)
                .collection(USER_MESSAGING_TOKENS)
    }

    fun getPostLikesReference(post: Post) : CollectionReference {
        return getPostReference(post)
                .collection(POST_LIKES)
    }

}