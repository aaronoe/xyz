package de.aaronoe.xyz.ui.feed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.XyzRepository

class FeedViewModel : ViewModel() {

    val posts by lazy { MutableLiveData<List<Post>>() }

    fun subscribeToPosts() {
        AccountManager.user?.let {
            XyzRepository.getUserFeedObservable(it)
                    .subscribeDefault { posts.value = it }
        }
    }

}