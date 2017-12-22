package de.aaronoe.xyz.ui.postdetail.comments

import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.utils.ListDiffUtilCallback

class CommentsDiff(oldList : List<Comment>, newList : List<Comment>) : ListDiffUtilCallback<Comment>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.getOrNull(oldItemPosition)?.id === newList.getOrNull(newItemPosition)?.id
    }
}