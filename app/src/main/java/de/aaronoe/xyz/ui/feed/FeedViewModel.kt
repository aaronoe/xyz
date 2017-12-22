package de.aaronoe.xyz.ui.feed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.XyzRepository
import io.reactivex.disposables.Disposable

class FeedViewModel : ViewModel() {

    val posts by lazy { MutableLiveData<List<Post>>() }
    private var feedDisposable : Disposable? = null

    fun subscribeToPosts() {
        AccountManager.user?.let {
            feedDisposable = XyzRepository.getUserFeedObservable(it)
                    .subscribeDefault { posts.value = it }
        }
    }

    override fun onCleared() {
        super.onCleared()
        feedDisposable?.dispose()
    }
}