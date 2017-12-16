package de.aaronoe.xyz.ui.userdetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.google.firebase.firestore.Query
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.repository.Firestore

/**
 *
 * Created by aoe on 11/25/17.
 */
class UserDetailViewModel(application: Application) : AndroidViewModel(application) {

    val postsLiveData by lazy { MutableLiveData<List<Post>>() }
    val userLiveData by lazy { MutableLiveData<User>() }

    val userName = ObservableField<String>()
    val userLocation = ObservableField<String>()
    val userPictureUrl = ObservableField<String>()
    val userBio = ObservableField<String>()
    val postCount = ObservableField<String>()
    val followingCount = ObservableField<String>()
    val followerCount = ObservableField<String>()

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
                        documentSnapshot.toObject(User::class.java).run {
                            userLiveData.value = this
                            setUser(this)
                        }
                    }
                }
    }

    fun setUser(user: User) {
        userName.set(user.userName)
        userLocation.set(user.location)
        userPictureUrl.set(user.pictureUrl)
        userBio.set(user.bio)
        postCount.set(user.postCount.toString())
        followerCount.set(user.followerCount.toString())
        followingCount.set(user.followingCount.toString())
    }

}