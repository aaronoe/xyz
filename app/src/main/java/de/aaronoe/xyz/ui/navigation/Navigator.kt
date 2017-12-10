package de.aaronoe.xyz.ui.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import de.aaronoe.xyz.R
import de.aaronoe.xyz.ui.feed.FeedFragment
import de.aaronoe.xyz.ui.newpost.NewPostFragment
import java.lang.ref.WeakReference

class Navigator(private val fragmentManager: FragmentManager,
                private val activity: WeakReference<AppCompatActivity>) {

    private val feedFragment by lazy { FeedFragment.newInstance() }
    private val newPostFragment by lazy { NewPostFragment.newInstance() }

    fun goToFeed() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(FeedFragment.TAG)
        if (fragment?.isVisible == true) {
            // TODO- aoe: scroll to top
        }
        val feed = fragment ?: feedFragment
        replaceFragment(feed, FeedFragment.TAG)
    }

    fun goToNewPost() {
        check(activity.get() is NavigationActivity)
        val fragment = fragmentManager.findFragmentByTag(NewPostFragment.TAG)
        val postFragment = fragment ?: newPostFragment
        replaceFragment(postFragment, NewPostFragment.TAG)
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