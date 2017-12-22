package de.aaronoe.xyz.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class DatabindingRecyclerViewAdapter : RecyclerView.Adapter<DatabindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return DatabindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DatabindingViewHolder, position: Int) {
        val viewModel = getViewModelForPosition(position)
        holder.bind(viewModel)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    @LayoutRes protected abstract fun getLayoutIdForPosition(position: Int) : Int

    protected abstract fun getViewModelForPosition(position: Int) : Any

}