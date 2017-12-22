package de.aaronoe.xyz.utils

import android.support.v7.util.DiffUtil

open class ListDiffUtilCallback<T>(val oldList : List<T>, val newList : List<T>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.getOrNull(oldItemPosition) === newList.getOrNull(newItemPosition)
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.getOrNull(oldItemPosition) === newList.getOrNull(newItemPosition)
    }
}