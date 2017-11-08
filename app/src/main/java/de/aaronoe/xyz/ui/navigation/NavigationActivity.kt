package de.aaronoe.xyz.ui.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import de.aaronoe.xyz.R
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.ui.login.LoginActivity
import org.jetbrains.anko.startActivity

class NavigationActivity : AppCompatActivity() {

    @BindView(R.id.main_nav_bottom_nav)
    lateinit var navigation : BottomNavigationView

    @BindView(R.id.main_nav_frame)
    lateinit var frame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        ButterKnife.bind(this)

        if (!AccountManager.isUserSet()) {
            // Go to login

        } else {
            // setup UI
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity<LoginActivity>()
                finish()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
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

}
