package de.aaronoe.xyz.ui.postdetail.comments

import android.support.v7.util.DiffUtil
import de.aaronoe.rxfirestore.subscribeComputation
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Comment
import de.aaronoe.xyz.utils.DatabindingRecyclerViewAdapter
import de.aaronoe.xyz.utils.ListDiffUtilCallback
import io.reactivex.Single

class CommentAdapter : DatabindingRecyclerViewAdapter() {

    var comments : List<Comment> = emptyList()
        set(value) {
            getDiffCallbackSingle(CommentsDiff(field, value))
                    .subscribeComputation {
                        it.dispatchUpdatesTo(this)
                        field = value
                    }
        }

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_comment

    override fun getViewModelForPosition(position: Int): Any {
        return CommentViewModel(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    fun <ItemType> DatabindingRecyclerViewAdapter.getDiffCallbackSingle(callback: ListDiffUtilCallback<ItemType>) : Single<DiffUtil.DiffResult> {
        return Single.create { emitter ->
            emitter.onSuccess(DiffUtil.calculateDiff(callback))
        }
    }

}