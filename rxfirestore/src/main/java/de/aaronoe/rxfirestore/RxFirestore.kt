package de.aaronoe.rxfirestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import io.reactivex.*

class NoSuchDocumentException : Exception("There is no document at the given DocumentReference")

inline fun <reified T> DocumentReference.getObservable(): Observable<T> {
    return Observable.create { emitter ->
        val listener = addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            documentSnapshot?.let {
                if (documentSnapshot.exists()) {
                    try {
                        emitter.onNext(documentSnapshot.toObject(T::class.java))
                    } catch (e : Exception) {
                        emitter.onError(e)
                    }
                } else {
                    emitter.onError(NoSuchDocumentException())
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}

inline fun <reified T> DocumentReference.getFlowable(backpressureStrategy: BackpressureStrategy): Flowable<T> {
    return Flowable.create( { emitter ->
        val listener = addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            documentSnapshot?.let {
                if (documentSnapshot.exists()) {
                    try {
                        emitter.onNext(documentSnapshot.toObject(T::class.java))
                    } catch (e : Exception) {
                        emitter.onError(e)
                    }
                } else {
                    emitter.onError(NoSuchDocumentException())
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }, backpressureStrategy)
}

inline fun <reified T> DocumentReference.getSingle() : Single<T> {
    return Single.create { emitter ->
        get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        try {
                            emitter.onSuccess(it.toObject(T::class.java))
                        } catch (e : Exception) {
                            emitter.onError(e)
                        }
                    } else {
                        emitter.onError(NoSuchDocumentException())
                    }
                }
                .addOnFailureListener { emitter.onError(it) }
    }
}

inline fun <reified T> CollectionReference.getObservable() : Observable<List<T>> {
    return Observable.create { emitter ->
        val listener = addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                try {
                    emitter.onNext(it.toObjects(T::class.java))
                } catch (e : Exception) {
                    emitter.onError(e)
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}

inline fun <reified T> CollectionReference.getFlowable(backpressureStrategy: BackpressureStrategy) : Flowable<List<T>> {
    return Flowable.create( { emitter ->
        val listener = addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                try {
                    emitter.onNext(it.toObjects(T::class.java))
                } catch (e : Exception) {
                    emitter.onError(e)
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }, backpressureStrategy)
}

inline fun <reified T> Query.getObservable() : Observable<List<T>> {
    return Observable.create { emitter ->
        val listener = addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                try {
                    emitter.onNext(it.toObjects(T::class.java))
                } catch (e : Exception) {
                    emitter.onError(e)
                }
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}


inline fun <reified T> CollectionReference.getSingle() : Single<List<T>> {
    return Single.create { emitter ->
        get()
                .addOnSuccessListener {
                    try {
                        emitter.onSuccess(it.toObjects(T::class.java))
                    } catch (e : Exception) {
                        emitter.onError(e)
                    }
                }
                .addOnFailureListener { emitter.onError(it) }
    }
}


inline fun <reified T> Query.getSingle() : Single<List<T>> {
    return Single.create { emitter ->
        get()
                .addOnSuccessListener {
                    try {
                        emitter.onSuccess(it.toObjects(T::class.java))
                    } catch (e : Exception) {
                        emitter.onError(e)
                    }
                }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun <T : Any> DocumentReference.setDocument(item : T) : Completable {
    return Completable.create { emitter ->
        set(item)
                .addOnCompleteListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun DocumentReference.deleteDocument() : Completable {
    return Completable.create { emitter ->
        delete()
                .addOnCompleteListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun <T : Any> Task<T>.getCompletable(): Completable {
    return Completable.create { emitter ->
        addOnSuccessListener { emitter.onComplete() }
        addOnFailureListener { emitter.onError(it) }
    }
}

fun <T : Any> CollectionReference.addDocumentSingle(item : T) : Single<DocumentReference> {
    return Single.create { emitter ->
        add(item)
                .addOnSuccessListener { emitter.onSuccess(it) }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun DocumentReference.updateDocumentCompletable(field : String, newValue : Any) : Completable {
    return Completable.create { emitter ->
        update(field, newValue)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun <ReturnType : Any> FirebaseFirestore.runTransactionCompletable(transaction: Transaction.Function<ReturnType>) : Single<ReturnType> {
    return Single.create { emitter ->
        runTransaction(transaction)
                .addOnSuccessListener { emitter.onSuccess(it) }
                .addOnFailureListener { emitter.onError(it) }
    }
}

fun WriteBatch.getCompletable() : Completable {
    return Completable.create { emitter ->
        commit()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }
}