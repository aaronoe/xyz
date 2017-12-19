package de.aaronoe.rxfirestore

import com.google.firebase.storage.UploadTask
import io.reactivex.Single


fun UploadTask.toSingle() : Single<UploadTask.TaskSnapshot> {
    return Single.create { emitter ->
        addOnSuccessListener { emitter.onSuccess(it) }
        addOnFailureListener { emitter.onError(it) }
    }
}