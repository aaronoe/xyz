package de.aaronoe.rxfirestore

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeDefault(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onNext, onError)
}

fun <T> Single<T>.subscribeDefault(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onSuccess, onError)
}

fun <T> Flowable<T>.subscribeDefault(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onSuccess, onError)
}

fun Completable.subscribeDefault(onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onComplete, onError)
}

fun <T> Observable<T>.subscribeDefault(onNext: (T) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onNext)
}

fun <T> Single<T>.subscribeDefault(onSuccess: (T) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onSuccess)
}

fun <T> Flowable<T>.subscribeDefault(onSuccess: (T) -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onSuccess)
}

fun Completable.subscribeDefault(onComplete: () -> Unit): Disposable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return subscribe(onComplete)
}