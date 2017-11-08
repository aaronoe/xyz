package de.aaronoe.xyz.ui.feed

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post
import de.hdodenhof.circleimageview.CircleImageView

class FeedAdapter(val context: Context) : RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder>() {

    private var postList : List<Post>? = null

    fun setPosts(posts : List<Post>) {
        postList = posts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder?, position: Int) {
        val post = postList?.get(position)
        post?.let { holder?.bind(context, it) }
    }

    override fun getItemCount(): Int {
        return if (postList != null) postList!!.size else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedItemViewHolder {
        return FeedItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.post_item, parent))
    }

    inner class FeedItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.feed_item_profile_name_tv)
        lateinit var profileNameTv : TextView
        @BindView(R.id.feed_item_profile_picture_iv)
        lateinit var profilePictureIv : CircleImageView
        @BindView(R.id.feed_item_post_image_iv)
        lateinit var postPictureIv : ImageView
        @BindView(R.id.post_item_like_checkbox)
        lateinit var likeCheckBox : CheckBox
        @BindView(R.id.post_item_comment_iv)
        lateinit var commentIv : ImageView
        @BindView(R.id.post_item_share_iv)
        lateinit var shareIv : ImageView
        @BindView(R.id.post_item_caption_tv)
        lateinit var captionTv : TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(context: Context, post: Post) {
            profileNameTv.text = post.author.userName
            captionTv.text = post.caption

            Glide.with(context)
                    .load(post.author.pictureUrl)
                    .into(profilePictureIv)

            Glide.with(context)
                    .load(post.mediaUrl)
                    .into(postPictureIv)
        }

    }

}