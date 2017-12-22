package de.aaronoe.xyz.utils

import android.databinding.BindingAdapter
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl : String?) {
    imageUrl?.let {
        Glide.with(imageView.context).load(it).into(imageView)
    }
}

@BindingAdapter("adapter")
fun setRecyclerAdapter(recyclerView: RecyclerView?, recyclerViewAdapter: DatabindingRecyclerViewAdapter) {
    recyclerView?.adapter = recyclerViewAdapter
}

@BindingAdapter("layoutManager")
fun setLayoutManager(recyclerView: RecyclerView?, orientation : Int) {
    recyclerView?.layoutManager = LinearLayoutManager(recyclerView?.context, orientation, false)
}

@BindingAdapter("dividers")
fun setItemDividers(recyclerView: RecyclerView?, orientation: Int) {
    recyclerView?.addItemDecoration(DividerItemDecoration(recyclerView.context, orientation))
}

@BindingAdapter("postDate")
fun setPostDate(textView: TextView?, timestamp: Long) {
    textView?.let {
        it.text = DateUtils.getGroupItemString(it.context, timestamp)
    }
}

@BindingAdapter("startAnimation")
fun startAnimation(view: View?, animation: Animation) {
    view?.startAnimation(animation)
}