package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.rxfirestore.getFlowable
import de.aaronoe.rxfirestore.getObservable
import de.aaronoe.rxfirestore.getSingle
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

        Firestore.getPostReference(post)
                .getSingle<Post>()
                .subscribeDefault( {
                    it
                } , {
                    it
                } )

        Firestore.getPostReference(post)
                .getObservable<Post>()
                .subscribeDefault( {
                    it
                } , {
                    it
                } )

        Firestore.getPostReference(post)
                .getFlowable<Post>(BackpressureStrategy.LATEST)
                .subscribeDefault( {
                    it
                } , {
                    it
                } )

        Firestore.getFeedPosts()?.let {
            it.getObservable<Comment>()
                    .subscribeDefault( {
                        it
                    } , {
                        it
                    } )
        }
    }

    fun <T> Observable<T>.subscribeDefault(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribeOn(Schedulers.io())
        observeOn(AndroidSchedulers.mainThread())
        subscribe(onNext, onError)
    }

    fun <T> Single<T>.subscribeDefault(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribeOn(Schedulers.io())
        observeOn(AndroidSchedulers.mainThread())
        subscribe(onSuccess, onError)
    }

    fun <T> Flowable<T>.subscribeDefault(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribeOn(Schedulers.io())
        observeOn(AndroidSchedulers.mainThread())
        subscribe(onSuccess, onError)
    }

    fun refreshComments(post: Post) {
        Firestore.getCommentsReference(post)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.let {
                        /*
                        comments.value = it.filter { it.exists() }.map {
                            it.toObject(Comment::class.java)
                        } */
                    }
                }
    }

}