package de.aaronoe.xyz.ui.userdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.aaronoe.xyz.R
import de.aaronoe.xyz.databinding.FragmentUserDetailBinding
import de.aaronoe.xyz.model.Post
import de.aaronoe.xyz.model.User
import org.parceler.Parcels

class UserFragment : Fragment() {

    private lateinit var user : User
    private lateinit var viewModel : UserDetailViewModel
    private lateinit var adapter : UserPostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && arguments!!.containsKey(USER_EXTRA)) {
            user = Parcels.unwrap(arguments!!.getParcelable(USER_EXTRA))
        } else throw IllegalStateException("No User present. Please use the Factory's newInstance()")

        viewModel = ViewModelProviders.of(this).get(UserDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding : FragmentUserDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        context?.let {
            adapter = UserPostsAdapter(it)
            binding.userPostsRv.run {
                layoutManager = GridLayoutManager(it, 3)
                adapter = this@UserFragment.adapter
            }
        }

        viewModel.postsLiveData.observe(this, Observer<List<Post>> {
            it?.let {
                adapter.posts = it
            }
        })
        viewModel.refreshUserPosts(user)

        viewModel.refreshUser(user)
        return binding.root
    }

    companion object Factory {
        private val USER_EXTRA = "USER_EXTRA"
        val TAG = "USER_FRAGMENT"

        fun newInstance(user : User) : UserFragment {
            val fragment = UserFragment()
            Bundle().run {
                putParcelable(USER_EXTRA, Parcels.wrap(user))
                fragment.arguments = this
                return fragment
            }
        }
    }
}