package de.aaronoe.xyz.ui.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import de.aaronoe.xyz.R
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.ui.feed.FeedFragment
import de.aaronoe.xyz.ui.newpost.NewPostFragment
import de.aaronoe.xyz.ui.search.SearchFragment
import de.aaronoe.xyz.ui.userdetail.UserFragment
import kotlinx.android.synthetic.main.fragment_feed.*
import java.lang.ref.WeakReference

class Navigator(private val fragmentManager: FragmentManager,
                private val activity: WeakReference<AppCompatActivity>) {

    private val feedFragment by lazy { FeedFragment.newInstance() }
    private val newPostFragment by lazy { NewPostFragment.newInstance() }
    private val userFragment by lazy { UserFragment.newInstance(AccountManager.user!!) }
    private val searchFragment by lazy { SearchFragment() }

    fun goToFeed() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(FeedFragment.TAG)
        val feed = fragment ?: feedFragment
        if (feed.isVisible) {
            feed.posts_rv.smoothScrollToPosition(0)
            return
        }
        replaceFragment(feed, FeedFragment.TAG)
    }

    fun goToNewPost() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(NewPostFragment.TAG)
        val postFragment = fragment ?: newPostFragment
        (activity.get() as? NavigationContract)?.let { (postFragment as NewPostFragment).router = it }
        replaceFragment(postFragment, NewPostFragment.TAG)
    }

    fun goToSearch() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(SearchFragment.TAG)
        val searchFragment = fragment ?: searchFragment
        replaceFragment(searchFragment, SearchFragment.TAG)
    }

    fun goToUser() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(UserFragment.TAG)
        val userFragment : Fragment = fragment ?: userFragment
        replaceFragment(userFragment, UserFragment.TAG)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = fragmentManager.beginTransaction()

        fragmentManager.primaryNavigationFragment?.let {
            transaction.detach(it)
        }
        if (fragmentManager.findFragmentByTag(tag) != null) {
            transaction.attach(fragment)
        } else {
            transaction.add(R.id.main_nav_frame, fragment, tag)
        }
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.commitAllowingStateLoss()
    }

}