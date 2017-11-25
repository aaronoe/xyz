package de.aaronoe.xyz.ui.userdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.repository.Firestore

/**
 *
 * Created by aoe on 11/25/17.
 */
class UserDetailViewModel : ViewModel() {

    val postsLiveData by lazy { MutableLiveData<List<Post>>() }

    val userLiveData by lazy { MutableLiveData<User>() }

    fun refreshUserPosts(user : User) {
        Firestore.getPostsForUser(user)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.let {
                        val postList  = mutableListOf<Post>()
                        it.forEach {
                            if (it.exists()) {
                                postList.add(it.toObject(Post::class.java))
                            }
                        }
                        postsLiveData.value = postList
                    }
                }
    }

    fun refreshUser(user : User) {
        Firestore.getUserReference(user)
                .addSnapshotListener { documentSnapshot, _ ->
                    if (documentSnapshot.exists()) {
                        userLiveData.value = documentSnapshot.toObject(User::class.java)
                    }
                }
    }

}