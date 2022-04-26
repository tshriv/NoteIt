package com.ts.NoteIt

import NoteIt.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userID", user.uid)
            intent.putExtra("userEmailorPhone", user.email ?: user.phoneNumber)
            startActivity(intent)
        } else {
           // setContentView(R.layout.activity_sing_in)

            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
            )

            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        }

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            /*  user?.uid?.let { Log.d("user id of firebase", it) }
              user?.email?.let { Log.d("user id of firebase", it) }
              user?.displayName?.let { Log.d("user id of firebase", it) } */

            Toast.makeText(this, "SignIn Success " + user.toString(), Toast.LENGTH_SHORT).show()
            Log.d("@@@uid_signInActivity", user?.uid.toString())

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userID", user?.uid)
            intent.putExtra("userEmailorPhone", user?.email)
            startActivity(intent)
            // ...
        } else {
            Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
            finishAffinity()
            System.exit(0)

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

}