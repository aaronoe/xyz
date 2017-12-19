package de.aaronoe.rxfirestore

import com.google.firebase.firestore.*
import io.reactivex.*


inline fun <reified T> DocumentReference.getObservable(): Observable<T> {
    return Observable.create { emitter ->
        val listener = addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
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

inline fun <reified T> DocumentReference.getFlowable(backpressureStrategy: BackpressureStrategy): Flowable<T> {
    return Flowable.create( { emitter ->
        val listener = addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            documentSnapshot?.let {
                if (documentSnapshot.exists()) {
                    emitter.onNext(documentSnapshot.toObject(T::class.java))
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
                .addOnSuccessListener { if (it.exists()) emitter.onSuccess(it.toObject(T::class.java)) }
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
                //emitter.onNext(it.filter { it.exists() }.map { it.toObject(T::class.java) })
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
                emitter.onNext(it.filter { it.exists() }.map { it.toObject(T::class.java) })
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
                emitter.onNext(it.filter { it.exists() }.map { it.toObject(T::class.java) })
            }
            firebaseFirestoreException?.let { emitter.onError(it) }
        }
        emitter.setCancellable { listener.remove() }
    }
}


inline fun <reified T> CollectionReference.getSingle() : Single<List<T>> {
    return Single.create { emitter ->
        get()
                .addOnSuccessListener { emitter.onSuccess(it.filter { it.exists() }.map { it.toObject(T::class.java) }) }
                .addOnFailureListener { emitter.onError(it) }
    }
}


inline fun <reified T> Query.getSingle() : Single<List<T>> {
    return Single.create { emitter ->
        get()
                .addOnSuccessListener { emitter.onSuccess(it.filter { it.exists() }.map { it.toObject(T::class.java) }) }
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