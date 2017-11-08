package de.aaronoe.xyz.ui.feed


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.utils.gone
import de.aaronoe.xyz.utils.visible


/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {

    @BindView(R.id.posts_rv)
    lateinit var postsRv : RecyclerView
    @BindView(R.id.empty_message_container)
    lateinit var emptyContainer : ViewGroup
    @BindView(R.id.feed_list_pb)
    lateinit var feedPb : ProgressBar

    private lateinit var viewModel : FeedViewModel
    private val layoutManager by lazy { LinearLayoutManager(this@FeedFragment.context, LinearLayoutManager.VERTICAL, false) }
    private val feedAdapter by lazy { this@FeedFragment.context?.let { FeedAdapter(it) } }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.posts.observe(this, Observer<List<Post>> {
            it?.let { it1 -> updateFeedList(it1) }
        })

        postsRv.layoutManager = layoutManager
        postsRv.adapter = feedAdapter

        viewModel.subscribeToPosts()
    }

    private fun updateFeedList(posts: List<Post>) {
        if (posts.isEmpty()) {
            feedPb.gone()
            postsRv.gone()
            emptyContainer.visible()
        } else {
            feedPb.gone()
            emptyContainer.gone()
            postsRv.visible()

            feedAdapter?.setPosts(posts)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        inflater.inflate(R.layout.fragment_feed, container, false).let {
            ButterKnife.bind(this@FeedFragment, it)
            return it
        }
    }

    companion object {
        fun newInstance() = FeedFragment()
    }

}

