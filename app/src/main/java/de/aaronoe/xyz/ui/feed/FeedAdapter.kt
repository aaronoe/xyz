package de.aaronoe.xyz.ui.feed

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.ui.postdetail.PostDetailActivity
import de.aaronoe.xyz.ui.userdetail.UserActivity
import de.aaronoe.xyz.utils.DateUtils
import de.hdodenhof.circleimageview.CircleImageView


class FeedAdapter(private val context: Context) : RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder>() {

    private var postList: List<Post>? = null
    private var lastPosition = -1


    fun setPosts(posts: List<Post>) {
        postList = posts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder?, position: Int) {
        val post = postList?.get(position)
        post?.let { holder?.bind(context, it) }
        holder?.itemView?.let { setAnimation(it, position) }
    }

    override fun getItemCount(): Int = if (postList != null) postList!!.size else 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedItemViewHolder {
        return FeedItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.post_item, parent, false))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    inner class FeedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.post_item_card)
        lateinit var postContainer : CardView
        @BindView(R.id.feed_item_profile_name_tv)
        lateinit var profileNameTv : TextView
        @BindView(R.id.feed_item_profile_picture_iv)
        lateinit var profilePictureIv : CircleImageView
        @BindView(R.id.feed_item_post_image_iv)
        lateinit var postPictureIv : ImageView
        @BindView(R.id.post_item_caption_tv)
        lateinit var captionTv : TextView
        @BindView(R.id.feed_item_comments_tv)
        lateinit var commentsTv : TextView
        @BindView(R.id.feed_item_likes_tv)
        lateinit var likesTv : TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(context: Context, post: Post) {
            profileNameTv.text =
                    context.getString(R.string.name_and_date, post.author.userName, DateUtils.getDefaultDateString(post.timestamp))
            captionTv.text = post.caption

            commentsTv.text = context.getString(R.string.number_of_comments, post.numberOfComments)
            likesTv.text = context.getString(R.string.number_of_likes, post.numberOfLikes)

            Glide.with(context)
                    .load(post.author.pictureUrl)
                    .into(profilePictureIv)

            Glide.with(context)
                    .load(post.mediaUrl)
                    .into(postPictureIv)

            itemView.setOnClickListener {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity, postContainer, context.getString(R.string.transition_key_post_container))
                context.startActivity(PostDetailActivity.getIntent(context, post), options.toBundle())
            }

            profilePictureIv.setOnClickListener {
                Toast.makeText(context, "Clicked on ${post.author.userName}", Toast.LENGTH_SHORT).show()
                val options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(context as Activity, profilePictureIv,
                                context.getString(R.string.user_picture_transition))
                context.startActivity(UserActivity.getIntent(context, post.author), options.toBundle())
            }
        }

    }
}