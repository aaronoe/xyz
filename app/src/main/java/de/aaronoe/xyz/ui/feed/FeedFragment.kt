package de.aaronoe.xyz.ui.feed


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.Firestore
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
    private lateinit var feedAdapter : FeedAdapter
    private lateinit var layoutManager : LinearLayoutManager

    private fun updateFeedList(posts: List<Post>) {
        if (posts.isEmpty()) {
            feedPb.gone()
            postsRv.gone()
            emptyContainer.visible()
        } else {
            feedPb.gone()
            emptyContainer.gone()
            postsRv.visible()

            feedAdapter.setPosts(posts)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        ButterKnife.bind(this@FeedFragment, view)

        context?.let { feedAdapter = FeedAdapter(it) }
        layoutManager = LinearLayoutManager(this@FeedFragment.context, LinearLayoutManager.VERTICAL, false)
        postsRv.layoutManager = layoutManager
        postsRv.adapter = feedAdapter

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.subscribeToPosts()
        viewModel.posts.observe(this, Observer<List<Post>> {
            it?.let { it1 -> updateFeedList(it1) }
        })

        return view
    }

    companion object {
        const val TAG = "FEED_FRAGMENT"
        fun newInstance() = FeedFragment()
    }

}

