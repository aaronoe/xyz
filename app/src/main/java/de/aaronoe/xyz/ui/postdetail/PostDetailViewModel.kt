package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.rxfirestore.getObservable
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore

class PostDetailViewModel : ViewModel() {

    val post = MutableLiveData<Post>()
    val comments = MutableLiveData<List<Comment>>()

    fun refreshPost(post: Post) {
        Firestore.getPostReference(post)
                .getObservable<Post>()
                .subscribeDefault { this.post.value = it }
    }

    fun refreshComments(post: Post) {
        Firestore.getCommentsReference(post)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .getObservable<Comment>()
                .subscribeDefault { comments.value = it }
    }

}