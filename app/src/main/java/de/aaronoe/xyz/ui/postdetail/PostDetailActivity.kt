package de.aaronoe.xyz.ui.postdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener
import de.aaronoe.xyz.R
import de.aaronoe.xyz.databinding.ActivityPostDetailBinding
import de.aaronoe.xyz.model.Post
import org.parceler.Parcels

class PostDetailActivity : AppCompatActivity() {

    lateinit var viewModel : PostDetailViewModel
    lateinit var bindings : ActivityPostDetailBinding
    lateinit var post : Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        viewModel = ViewModelProviders.of(this).get(PostDetailViewModel::class.java)
        bindings.viewModel = viewModel

        viewModel.post.observe(this, Observer {
            it?.apply {
                rebindPost(this)
            }
        })

        bindings.dragFrame.addListener(object : ElasticDragDismissListener {
            override fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float, rawOffset: Float, rawOffsetPixels: Float) {
            }

            override fun onDragDismissed() {
                finishAfterTransition()
            }
        })

        if (intent.hasExtra(POST_EXTRA)) {
            post = Parcels.unwrap(intent.getParcelableExtra(POST_EXTRA))
            rebindPost(post)
            viewModel.refreshComments(post)
            viewModel.refreshPost(post)
        }
    }

    private fun rebindPost(post: Post) {
        bindings.post = post
        bindings.executePendingBindings()
    }

    override fun onBackPressed() {
        viewModel.itemAnimation.set(AnimationUtils.loadAnimation(this, R.anim.bottom_down))
        super.onBackPressed()
    }

    companion object Factory {
        fun getIntent(context: Context, post: Post): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra(POST_EXTRA, Parcels.wrap(post))
            }
        }

        private val POST_EXTRA = "POST_EXTRA"
    }

}
