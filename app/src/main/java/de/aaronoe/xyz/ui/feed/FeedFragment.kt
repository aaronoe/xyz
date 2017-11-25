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
    @BindView(R.id.fab_add)
    lateinit var addFab : FloatingActionButton

    private lateinit var viewModel : FeedViewModel
    private val layoutManager by lazy { LinearLayoutManager(this@FeedFragment.context, LinearLayoutManager.VERTICAL, false) }
    private val feedAdapter by lazy { this@FeedFragment.context?.let { FeedAdapter(it) } }

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
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        ButterKnife.bind(this@FeedFragment, view)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.posts.observe(this, Observer<List<Post>> {
            it?.let { it1 -> updateFeedList(it1) }
        })

        postsRv.layoutManager = layoutManager
        postsRv.adapter = feedAdapter

        viewModel.subscribeToPosts()

        addFab.setOnClickListener {
            AccountManager.user?.let { it1 ->
                Post(it1,
                        "https://images.unsplash.com/photo-1494516192674-b82b5f1e61dc?auto=format&fit=crop&w=633&q=60&ixid=dW5zcGxhc2guY29tOzs7Ozs%3D",
                        "Aaron told Floyd that he is indeed a very good boy. Floyd likes to watch Football with his two daddies")
            }?.let { it1 -> Firestore.makeUserTestPost(it1) }
        }

        return view
    }

    companion object {
        fun newInstance() = FeedFragment()
    }

}

