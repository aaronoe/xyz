package de.aaronoe.xyz.utils

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
