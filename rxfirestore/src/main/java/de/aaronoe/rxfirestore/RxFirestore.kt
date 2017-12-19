package de.aaronoe.rxfirestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


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

inline fun <reified T> getDocumentSingle(reference: DocumentReference) : Single<T> {
    return Single.create { emitter ->
        reference.get()
                .addOnSuccessListener { if (it.exists()) emitter.onSuccess(it.toObject(T::class.java)) }
                .addOnFailureListener { emitter.onError(it) }
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



inline fun <reified T> getCollectionSingle(reference: CollectionReference) : Single<List<T>> {
    return Single.create { emitter ->
        reference.get()
                .addOnSuccessListener { emitter.onSuccess(it.filter { it.exists() }.map { it.toObject(T::class.java) }) }
                .addOnFailureListener { emitter.onError(it) }
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

fun <T : Any> setDocument(item : T, documentReference: DocumentReference) : Completable {
    return Completable.create { emitter ->
        documentReference.set(item)
                .addOnCompleteListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun deleteDocument(documentReference: DocumentReference) : Completable {
    return Completable.create { emitter ->
        documentReference.delete()
                .addOnCompleteListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}