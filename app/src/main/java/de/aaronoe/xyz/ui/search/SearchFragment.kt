package de.aaronoe.xyz.ui.search

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.aaronoe.xyz.R
import de.aaronoe.xyz.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    lateinit var viewModel : SearchViewModel
    lateinit var binding : FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        return binding.root
    }

    companion object {
        const val TAG = "SEARCH_FRAGMENT"
    }

}