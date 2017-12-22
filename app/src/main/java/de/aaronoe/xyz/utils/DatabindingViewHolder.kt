package de.aaronoe.xyz.utils

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.android.databinding.library.baseAdapters.BR

class DatabindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel : Any) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

}