package de.aaronoe.xyz.ui.search.results

import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.utils.DatabindingRecyclerViewAdapter

class SearchResultAdapter : DatabindingRecyclerViewAdapter() {

    var searchResults = emptyList<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_search_result

    override fun getViewModelForPosition(position: Int): Any = SearchResultViewModel(searchResults[position])

    override fun getItemCount(): Int = searchResults.size
}