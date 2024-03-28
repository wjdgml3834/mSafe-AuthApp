package com.example.msafe.ui.viewModel

import android.app.Application
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class OneClickSIgnInViewModel(application: Application) : AndroidViewModel(application) {

    var user: FirebaseUser? = null

    init {
        user = Firebase.auth.currentUser
    }

    @Composable
    fun rememberFirebaseAuthLauncher(
        onAuthComplete: (AuthResult) -> Unit,
        onAuthError: (java.lang.Exception) -> Unit
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {

            val scope = rememberCoroutineScope()
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                scope.launch {
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    onAuthComplete(authResult)
                }
            } catch (e: ApiException) {
                onAuthError(e)
            }
        }
    }




    // Function to sign in using email and password
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): AuthResult? {
        return try {
            // Create the user in Firebase authentication with the provided email and password
            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()

            // Return the authentication result
            return authResult
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
            return null
        }
    }

    suspend fun signUp(email: String, password: String, userName: String): AuthResult? {
        return try {
            // Create the user in Firebase authentication with the provided email and password
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()

            // Update the user's display name with the provided username
            val user = authResult.user
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()
            user?.updateProfile(profileUpdates)?.await()

            // Return the authentication result
            return authResult
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
            return null
        }
    }

    fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("updated")
                } else {
                    println("something went wrong")
                }
            }
    }

//send email
    suspend fun sendLinkEmail() {
        val email = "zaterdag98@gmail.com"
        val password = "crcpzguyfmugevhs"

        withContext(Dispatchers.IO) {
            try {
                //set the needed properties for the email configeration
                val properties = Properties().apply {

                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")

                }
                //connect to email server
                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(email, password)
                    }
                })
                // the message to be sent
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(email))
                    setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(user?.email.toString())
                    )
                    subject = "link of the website"
                    setText("https://stackoverflow.com/questions/73365098/how-to-turn-on-less-secure-app-access-on-google")//nothing important just the link where i found the solution how to use gmail
                }
                //sending the email using the SMTP  configeration
                Transport.send(message)

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
                e.message
            }
        }
    }

}
