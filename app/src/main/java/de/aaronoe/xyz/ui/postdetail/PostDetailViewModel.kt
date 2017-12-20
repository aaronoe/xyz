package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.XyzRepository
import io.reactivex.disposables.Disposable

class PostDetailViewModel : ViewModel() {

    val post = MutableLiveData<Post>()
    val comments = MutableLiveData<List<Comment>>()
    private var postDisposable : Disposable? = null
    private var commentDisposable : Disposable? = null

    fun refreshPost(post: Post) {
        postDisposable = XyzRepository
                .getPostObservable(post)
                .subscribeDefault { this.post.value = it }
    }

    fun refreshComments(post: Post) {
        commentDisposable = XyzRepository
                .getCommentsForPostObservable(post)
                .subscribeDefault { comments.value = it }
    }

    override fun onCleared() {
        super.onCleared()
        commentDisposable?.dispose()
        postDisposable?.dispose()
    }
}