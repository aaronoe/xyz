package de.aaronoe.xyz.ui.userdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.utils.gone
import de.aaronoe.xyz.utils.visible
import de.hdodenhof.circleimageview.CircleImageView
import org.parceler.Parcels

class UserActivity : AppCompatActivity() {

    @BindView(R.id.user_toolbar)
    lateinit var toolbar : Toolbar
    @BindView(R.id.user_name_tv)
    lateinit var usernameTv : TextView
    @BindView(R.id.user_photo_iv)
    lateinit var userphotoIv : CircleImageView
    @BindView(R.id.user_posts_rv)
    lateinit var userPostsRv : RecyclerView
    @BindView(R.id.user_detail_nr_followers_tv)
    lateinit var userFollowerCountTv : TextView
    @BindView(R.id.user_detail_nr_following_tv)
    lateinit var userFollowingCountTv : TextView
    @BindView(R.id.user_detail_nr_posts_tv)
    lateinit var userPostsCountTv : TextView
    @BindView(R.id.user_bio_tv)
    lateinit var userBioTv : TextView
    @BindView(R.id.user_location_tv)
    lateinit var userLocationTv : TextView
    @BindView(R.id.draggable_frame)
    lateinit var dragFrame : ElasticDragDismissFrameLayout

    private lateinit var user : User
    private lateinit var viewModel : UserDetailViewModel
    private lateinit var adapter : UserPostsAdapter

    companion object Factory {
        val USER_EXTRA = "USER_EXTRA"

        fun getIntent(context : Context, user : User) : Intent {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(USER_EXTRA, Parcels.wrap(user))
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        supportPostponeEnterTransition()
        ButterKnife.bind(this)

        dragFrame.addListener(object : ElasticDragDismissListener {
            override fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float, rawOffset: Float, rawOffsetPixels: Float) {
            }

            override fun onDragDismissed() {
                finishAfterTransition()
            }
        })

        user = Parcels.unwrap(intent.getParcelableExtra(USER_EXTRA))
        viewModel = ViewModelProviders.of(this).get(UserDetailViewModel::class.java)

        makePostsSubscription()
        makeUserSubscription()
        populateUserInfo()
    }

    private fun makeUserSubscription() {
        viewModel.userLiveData.observe(this, Observer {
            it?.let { user = it }
            populateUserInfo()
        })
        viewModel.refreshUser(user)
    }

    private fun makePostsSubscription() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        userPostsRv.layoutManager = gridLayoutManager
        adapter = UserPostsAdapter(this)
        userPostsRv.adapter = adapter

        viewModel.postsLiveData.observe(this, Observer {
            populatePostsRv(it.orEmpty())
        })

        viewModel.refreshUserPosts(user)
    }

    private fun populatePostsRv(posts: List<Post>) {
        if (posts.isNotEmpty()) {
            adapter.posts = posts
        } else {

        }
    }

    private fun populateUserInfo() {
        usernameTv.text = user.userName
        userPostsCountTv.text = user.postCount.toString()
        userFollowerCountTv.text = user.followerCount.toString()
        userFollowingCountTv.text = user.followingCount.toString()

        if (user.location.isBlank()) {
            userLocationTv.gone()
        } else {
            userLocationTv.text = user.location
            userLocationTv.visible()
        }

        if (user.bio.isBlank()) {
            userBioTv.gone()
        } else {
            userBioTv.text = user.bio
            userBioTv.visible()
        }

        Glide.with(this)
                .load(user.pictureUrl)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
                        userphotoIv.setImageDrawable(resource)
                        supportStartPostponedEnterTransition()
                    }
                })

    }

}