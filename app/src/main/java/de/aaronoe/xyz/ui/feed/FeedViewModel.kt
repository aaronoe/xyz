package de.aaronoe.xyz.ui.feed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore
import java.util.*

class FeedViewModel : ViewModel() {

    val posts by lazy { MutableLiveData<List<Post>>() }


    fun subscribeToPosts() {
        Firestore.getFeedPosts()?.addSnapshotListener { snapshot, exception ->
            snapshot?.let {
                val postList = Collections.emptyList<Post>()
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