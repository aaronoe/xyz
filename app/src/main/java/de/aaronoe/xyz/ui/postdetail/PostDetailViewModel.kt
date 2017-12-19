package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import de.aaronoe.rxfirestore.getDocumentSingle
import de.aaronoe.rxfirestore.getDocumentSinglee
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.repository.Firestore
import de.aaronoe.xyz.repository.getCollectionObservable
import de.aaronoe.xyz.repository.getDocumentObservable
import io.reactivex.Observable
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

        getDocumentObservable<Post>(Firestore.getPostReference(post))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( { post ->
                    post
                }, { throwable ->
                    throwable
                } )

        getDocumentSingle<Post>(Firestore.getPostReference(post))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { post, throwable ->
                    post
                    throwable
                }

        Firestore.getFeedPosts()?.let {
            getCollectionObservable<Post>(it)
                    .subscribeDefault({ posts ->
                        posts
                    }, { throwable ->
                        throwable
                    })
        }

        Firestore.getPostReference(post).getDocumentSinglee<Post>()
    }

    fun <T> Observable<T>.subscribeDefault(onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribeOn(Schedulers.io())
        observeOn(AndroidSchedulers.mainThread())
        subscribe(onNext, onError)
    }

    fun refreshComments(post: Post) {
        Firestore.getCommentsReference(post)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.let {
                        comments.value = it.filter { it.exists() }.map {
                            it.toObject(Comment::class.java)
                        }
                    }
                }
    }

}