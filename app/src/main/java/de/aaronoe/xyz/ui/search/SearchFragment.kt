package de.aaronoe.xyz.ui.search

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lapism.searchview.SearchAdapter
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.R
import de.aaronoe.xyz.databinding.FragmentSearchBinding
import de.aaronoe.xyz.repository.XyzRepository
import de.aaronoe.xyz.utils.asObservable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    lateinit var viewModel : SearchViewModel
    lateinit var binding : FragmentSearchBinding
    lateinit var adapter : SearchAdapter

    private var searchDisposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        searchDisposable = binding.searchview.asObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .switchMapSingle { XyzRepository.searchUsers(it) }
                .subscribeDefault {
                    viewModel.searchedUsers.value = it
                    viewModel.adapter.searchResults = it
                }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchDisposable?.dispose()
    }

    companion object {
        const val TAG = "SEARCH_FRAGMENT"
    }

}