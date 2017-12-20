package de.aaronoe.xyz.ui.userdetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.repository.XyzRepository
import io.reactivex.disposables.Disposable

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

    private var postsDisposable : Disposable? = null
    private var userDisposable : Disposable? = null

    fun refreshUserPosts(user : User) {
        postsDisposable = XyzRepository
                .getPostsForUserObservable(user)
                .subscribeDefault { postsLiveData.value = it }
    }

    fun refreshUser(user : User) {
        userDisposable = XyzRepository
                .getUserObservable(user)
                .subscribeDefault {
                    userLiveData.value = it
                    setUser(it)
                }
    }

    override fun onCleared() {
        super.onCleared()
        postsDisposable?.dispose()
        userDisposable?.dispose()
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