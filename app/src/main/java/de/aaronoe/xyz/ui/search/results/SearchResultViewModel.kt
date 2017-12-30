package de.aaronoe.xyz.ui.search.results

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.ui.userdetail.UserActivity
import de.hdodenhof.circleimageview.CircleImageView

class SearchResultViewModel(val user: User) {

    fun onClickItem(view : View?) {
        view?.let {
            view.findViewById<CircleImageView>(R.id.search_result_iv)?.apply {
                val options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(context as Activity, this,
                                context.getString(R.string.user_picture_transition))
                context.startActivity(UserActivity.getIntent(context, user), options.toBundle())
            }
        }
    }

}