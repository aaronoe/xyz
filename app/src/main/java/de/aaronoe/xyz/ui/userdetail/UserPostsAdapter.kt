package de.aaronoe.xyz.ui.userdetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post

/**
 *
 * Created by aoe on 11/25/17.
 */
class UserPostsAdapter(val context: Context) : RecyclerView.Adapter<UserPostsAdapter.UserPostsViewHolder>() {

    var posts : List<Post>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: UserPostsViewHolder?, position: Int) {
        val post = posts?.get(position)
        post?.let { holder?.bind(context, it) }
    }

    override fun getItemCount(): Int =
            if (posts != null) posts!!.size else 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserPostsViewHolder =
            UserPostsViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.user_post_item, parent, false))

    inner class UserPostsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.user_post_item_iv)
        lateinit var userPostIv : ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(context : Context, post : Post) {
            Glide.with(context)
                    .load(post.mediaUrl)
                    .into(userPostIv)
        }

    }

}