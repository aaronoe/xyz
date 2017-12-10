package de.aaronoe.xyz.ui.newpost

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.rengwuxian.materialedittext.MaterialEditText
import de.aaronoe.xyz.R
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.Firestore
import de.aaronoe.xyz.ui.navigation.NavigationContract
import de.aaronoe.xyz.utils.SquareImageView
import de.aaronoe.xyz.utils.gone
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 *
 * Created by aoe on 11/25/17.
 */
class NewPostFragment : Fragment() {

    @BindView(R.id.new_post_photo_container)
    lateinit var photoContainer : FrameLayout
    @BindView(R.id.new_post_preview_iv)
    lateinit var previewPhotoIv : SquareImageView
    @BindView(R.id.new_post_empty_container)
    lateinit var emptyContainer : LinearLayout
    @BindView(R.id.new_post_add_fab)
    lateinit var addPostFab : FloatingActionButton
    @BindView(R.id.new_post_description_edittext)
    lateinit var descriptionEditText : MaterialEditText

    lateinit var router : NavigationContract
    private var imageFile : File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_newpost, container, false)

        ButterKnife.bind(this, view)
        photoContainer.setOnClickListener {
            EasyImage.openChooserWithGallery(this, "Choose Image for Post", 0)
        }
        addPostFab.setOnClickListener { finalizePost() }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                loadPreviewImage(imageFile)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
            }
        })
    }

    private fun loadPreviewImage(imageFile : File) {
        emptyContainer.gone()
        this.imageFile = imageFile

        Glide.with(context)
                .load(imageFile.absoluteFile)
                .into(previewPhotoIv)
    }

    private fun finalizePost() {
        if (imageFile == null) return
        val description = descriptionEditText.text.toString()
        AccountManager.user?.let {
            Firestore.createNewPost(it, description, imageFile!!)
        }

        router.goToFeed()
    }

    companion object {
        const val TAG = "NEW_POST_FRAGMENT"
        fun newInstance() = NewPostFragment()
    }

}