package de.aaronoe.xyz.ui.navigation

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import de.aaronoe.xyz.R
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.ui.login.LoginActivity
import java.lang.ref.WeakReference

class NavigationActivity : AppCompatActivity(), NavigationContract {

    @BindView(R.id.main_nav_bottom_nav)
    lateinit var navigation : BottomNavigationView

    private lateinit var navigator : Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        ButterKnife.bind(this)
        navigator = Navigator(supportFragmentManager, WeakReference(this))

        if (!AccountManager.isUserSet()) {
            // Go to login
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // setup UI
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //navigation.selectedItemId = R.id.navigation_home
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                navigator.goToFeed()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
                navigator.goToNewPost()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_activity -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun goToFeed() {
        navigation.selectedItemId = R.id.navigation_home
    }
}
