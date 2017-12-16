package de.aaronoe.xyz.utils

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl : String?) {
    imageUrl?.let {
        Glide.with(imageView.context).load(it).into(imageView)
    }
}