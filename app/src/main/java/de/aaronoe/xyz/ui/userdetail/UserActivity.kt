package de.aaronoe.xyz.ui.userdetail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.User
import de.hdodenhof.circleimageview.CircleImageView
import org.parceler.Parcels

class UserActivity : AppCompatActivity() {

    @BindView(R.id.user_toolbar)
    lateinit var toolbar : Toolbar
    @BindView(R.id.user_name_tv)
    lateinit var usernameTv : TextView
    @BindView(R.id.user_photo_iv)
    lateinit var userphotoIv : CircleImageView
    @BindView(R.id.user_tabs)
    lateinit var tabs : TabLayout
    @BindView(R.id.user_viewpager)
    lateinit var viewpager : ViewPager

    lateinit var user : User

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

        user = Parcels.unwrap(intent.getParcelableExtra(USER_EXTRA))
        populateUserInfo()
    }

    private fun populateUserInfo() {
        usernameTv.text = user.userName
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