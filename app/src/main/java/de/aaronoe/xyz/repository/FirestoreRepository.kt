package de.aaronoe.xyz.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.Observable


inline fun <reified T> getDocumentObservable(reference: DocumentReference): Observable<T> {
    return Observable.create { emitter ->
        val listener = reference.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            documentSnapshot?.let {
                if (documentSnapshot.exists()) {
                    emitter.onNext(documentSnapshot.toObject(T::class.java))
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}

fun getDocumentSnapshotObservable(reference: DocumentReference): Observable<DocumentSnapshot> {
    return Observable.create { emitter ->
        val listener = reference.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            documentSnapshot?.let {
                if (documentSnapshot.exists()) {
                    emitter.onNext(documentSnapshot)
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}

inline fun <reified T> getCollectionObservable(reference: CollectionReference) : Observable<List<T>> {
    return Observable.create { emitter ->
        val listener = reference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                emitter.onNext(it.filter { it.exists() }.map { it.toObject(T::class.java) })
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}


fun getCollectionSnapshotObservable(reference: CollectionReference) : Observable<List<DocumentSnapshot>> {
    return Observable.create { emitter ->
        val listener = reference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                emitter.onNext(it.filter { it.exists() })
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}