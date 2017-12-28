package de.aaronoe.xyz.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.ui.search.results.SearchResultAdapter

class SearchViewModel : ViewModel() {

    val searchedUsers by lazy { MutableLiveData<List<User>>() }

    val adapter by lazy { SearchResultAdapter() }

}