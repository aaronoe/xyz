package de.aaronoe.xyz.ui.search

import android.arch.lifecycle.ViewModel
import de.aaronoe.xyz.ui.postdetail.comments.CommentAdapter

class SearchViewModel : ViewModel() {

    val adapter : CommentAdapter = CommentAdapter()

}