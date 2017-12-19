package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore

class PostDetailViewModel : ViewModel() {

    val post = MutableLiveData<Post>()
    val comments = MutableLiveData<List<Comment>>()

    fun refreshPost(post: Post) {
        Firestore.getPostReference(post)
                .addSnapshotListener { documentSnapshot, _ ->
                    documentSnapshot?.let {
                        if (documentSnapshot.exists()) {
                            this.post.value = documentSnapshot.toObject(Post::class.java)
                        }
                    }
                }
    }

    fun refreshComments(post: Post) {
        Firestore.getCommentsReference(post)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.let {
                        comments.value = it.filter { it.exists() }.map {
                            it.toObject(Comment::class.java)
                        }
                    }
                }
    }

}