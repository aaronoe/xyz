package de.aaronoe.xyz.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import de.aaronoe.rxfirestore.subscribeDefault
import de.aaronoe.xyz.R
import de.aaronoe.xyz.model.User
import de.aaronoe.xyz.repository.AccountManager
import de.aaronoe.xyz.repository.XyzRepository
import de.aaronoe.xyz.ui.navigation.NavigationActivity
import io.reactivex.Single
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    @BindView(R.id.sign_in_button)
    lateinit var signInButton: SignInButton

    private lateinit var progressDialog: ProgressDialog
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)
        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        signInButton.setOnClickListener { onClickSignIn() }
        progressDialog = indeterminateProgressDialog("Logging you in", "Please Wait")

        updateUI(firebaseAuth.currentUser)
    }

    private fun onClickSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data))
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess || result.signInAccount == null) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            acct?.let {
                progressDialog.show()
                firebaseAuthWithGoogle(it)
            }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        firebaseAuth.signInWithCredential(credential)
                .loginSingle()
                .flatMapCompletable { XyzRepository.getCreateUserAccountCompletable(User(it.user)) }
                .subscribeDefault(
                        onComplete = {
                            toast("Success")
                            AccountManager.onFirstLogin()
                            progressDialog.cancel()
                            updateUI(firebaseAuth.currentUser)
                        },
                        onError = {
                            toast("Failure")
                            progressDialog.cancel()
                        }
                )
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            finish()
            startActivity(intentFor<NavigationActivity>().singleTop().clearTop())
        }
    }

    private fun Task<AuthResult>.loginSingle() : Single<AuthResult> {
        return Single.create { emitter ->
            addOnSuccessListener { emitter.onSuccess(it) }
            addOnFailureListener { emitter.onError(it) }
        }
    }

}