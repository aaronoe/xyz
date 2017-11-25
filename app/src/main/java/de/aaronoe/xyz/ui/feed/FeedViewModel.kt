package de.aaronoe.xyz.ui.feed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore

class FeedViewModel : ViewModel() {

    val posts by lazy { MutableLiveData<List<Post>>() }

    fun subscribeToPosts() {
        Firestore.getFeedPosts()?.orderBy("timestamp", Query.Direction.DESCENDING)?.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                val postList  = mutableListOf<Post>()
                it.forEach {
                    if (it.exists()) {
                        postList.add(it.toObject(Post::class.java))
                    }
                }
                posts.value = postList
            }
        }
    }

}