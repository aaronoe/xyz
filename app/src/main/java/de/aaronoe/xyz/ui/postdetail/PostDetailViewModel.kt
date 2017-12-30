package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import android.view.animation.AnimationUtils
import de.aaronoe.rxfirestore.incrementLongField
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.R
import de.aaronoe.xyz.messaging.NotificationFactory
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore
import de.aaronoe.xyz.repository.XyzRepository
import de.aaronoe.xyz.ui.postdetail.comments.CommentAdapter
import de.aaronoe.xyz.utils.gone
import de.aaronoe.xyz.utils.visible
import de.aaronoe.xyz.xyzApp
import io.reactivex.disposables.Disposable

class PostDetailViewModel : ViewModel() {

    val post = MutableLiveData<Post>()
    val comments = MutableLiveData<List<Comment>>()
    val commentsLoadingVisibility = ObservableField(View.VISIBLE)
    val commentsListVisibility = ObservableField(View.GONE)
    val commentsErrorVisibility = ObservableField(View.GONE)

    val commentAdapter by lazy { CommentAdapter() }
    val itemAnimation = ObservableField(AnimationUtils.loadAnimation(xyzApp.instance, R.anim.slide_in_bottom))

    private var postDisposable : Disposable? = null
    private var commentDisposable : Disposable? = null

    fun refreshPost(post: Post) {
        postDisposable = XyzRepository
                .getPostObservable(post)
                .subscribeDefault(onNext = { this.post.value = it },
                        onError = {})
    }

    fun refreshComments(post: Post) {
        commentDisposable = XyzRepository
                .getCommentsForPostObservable(post)
                .subscribeDefault( onNext = {
                    comments.value = it
                    commentAdapter.comments = it
                    commentsListVisibility.visible()
                    commentsLoadingVisibility.gone()
                    commentsErrorVisibility.gone()
                }, onError = {
                    commentsListVisibility.gone()
                    commentsLoadingVisibility.gone()
                    commentsErrorVisibility.visible()
                })
    }

    fun newComment(view: View?) {
        post.value?.let {
        }
    }

    override fun onCleared() {
        super.onCleared()
        commentDisposable?.dispose()
        postDisposable?.dispose()
    }
}