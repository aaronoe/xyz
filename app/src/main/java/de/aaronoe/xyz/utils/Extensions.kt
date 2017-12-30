package de.aaronoe.xyz.utils

import io.reactivex.subjects.PublishSubject
import org.cryse.widget.persistentsearch.PersistentSearchView


fun PersistentSearchView.asObservable(): PublishSubject<String> {

    val subject = PublishSubject.create<String>()

    setSearchListener(object : PersistentSearchView.SearchListener {
        override fun onSearch(query: String?) {
            query?.let {
                subject.onNext(it)
            }
        }

        override fun onSearchExit() {
        }

        override fun onSearchCleared() {
        }

        override fun onSearchEditClosed() {
        }

        override fun onSearchTermChanged(term: String?) {
            term?.let { subject.onNext(it) }
        }

        override fun onSearchEditBackPressed(): Boolean = true

        override fun onSearchEditOpened() {
        }
    })

    return subject

}