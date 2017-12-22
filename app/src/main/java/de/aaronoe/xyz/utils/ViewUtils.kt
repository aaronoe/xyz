package de.aaronoe.xyz.utils

import android.databinding.ObservableField
import android.view.View


fun View.gone() {
    if (this.visibility == View.GONE) return
    this.visibility = View.GONE
}

fun View.invisible() {
    if (this.visibility == View.INVISIBLE) return
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    if (this.visibility == View.VISIBLE) return
    this.visibility = View.VISIBLE
}

fun ObservableField<Int>.gone() {
    if (this.get() == View.GONE) return
    this.set(View.GONE)
}

fun ObservableField<Int>.visible() {
    if (this.get() == View.VISIBLE) return
    this.set(View.VISIBLE)
}

fun ObservableField<Int>.invisible() {
    if (this.get() == View.INVISIBLE) return
    this.set(View.INVISIBLE)
}