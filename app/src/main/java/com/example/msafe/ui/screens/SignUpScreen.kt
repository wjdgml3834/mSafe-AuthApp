package com.example.msafe.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.msafe.R
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.theme.BlueDarkMsafe
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.util.Resource
import com.google.api.ResourceProto.resource
import kotlinx.coroutines.delay
import java.util.HashMap


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(navController: NavController, viewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel) {
    val context = LocalContext.current

    Scaffold(

    ) {

        signUp(navController, viewModel, changeLanguageViewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 770.dp),
            horizontalArrangement = Arrangement.End,
            ) {
            TextButton(modifier = Modifier
                .width(250.dp)
                .padding(end =200.dp, bottom = 15.dp)
                .height(50.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                onClick = {
                    navController.navigate(Screens.SignInScreen.route)
                }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "back",
                    Modifier.size(50.dp),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }

            /*
            Button(modifier = Modifier
                .width(70.dp)
                .height(50.dp).padding(end = 10.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueLightMsafe,
                    contentColor = androidx.compose.ui.graphics.Color.White
                ),
                onClick = {
                    changeLanguageViewModel.changeLanguage(context, "nl")
                    changeLanguageViewModel.saveLanguage(context, "nl")
                }) {
                Text(text = "NL")
            }
            Button(modifier = Modifier
                .width(75.dp)
                .height(50.dp).padding(end = 15.dp),
                shape = RoundedCornerShape(25),

                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueLightMsafe,
                    contentColor = androidx.compose.ui.graphics.Color.White,
                ),

                onClick = {

                    changeLanguageViewModel.changeLanguage(context, "en")
                    changeLanguageViewModel.saveLanguage(context, "en")

                }) {
                Text(text = "EN")
            }

             */

            //draw flag
            FloatingActionButton(modifier = Modifier
                .padding(start = 85.dp, top = 2.dp, end = 15.dp)
                .size(45.dp),
                backgroundColor = androidx.compose.ui.graphics.Color.Transparent,
                onClick = {
                    navController.navigate(Screens.LanguageScreen.route)
                }) {
                Image(
                    painterResource(Translate().getResource(changeLanguageViewModel.language.value)),
                    "flag"
                )
            }
        }

    }

}


@Composable
fun signUp(navController: NavController, viewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel) {

    val token = stringResource(R.string.your_web_client_id)
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var firstPassword by remember { mutableStateOf("") }
    var secondPassword by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    // Add a mutable state variable to track if the email is already used
    var isEmailAlreadyUsed by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val launcher = viewModel.rememberFirebaseAuthLauncher(onAuthComplete = { result ->
        viewModel.user = result.user
        navController.navigate(Screens.HomeScreen.route)
    }, onAuthError = {
        viewModel.user = null
    })

    //top light bar
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 780.dp)
    ) {
        drawRect(color = BlueVeryLightMsafe)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        //logo
        Image(
            modifier = Modifier.padding(top = 100.dp),
            painter = painterResource(id = R.drawable.msafe),
            contentDescription = "logo"
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)

        ) {
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.register)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) }
            OutlinedButton(modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueLightMsafe
                ),
                shape = RoundedCornerShape(25),
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token).requestEmail().build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)


                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google logo",
                    )
                    changeLanguageViewModel.resourceHashMap.value?.get(R.string.google_sign_in)?.let {
                        Text(
                            it, modifier = Modifier.padding(start = 15.dp),
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }

            }
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.user_name)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) }
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth(),
            )
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.email)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) }
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                isError = isEmailAlreadyUsed, // Set the error state based on whether the email is already used
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth()
            )
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.password)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) }
            OutlinedTextField(
                value = firstPassword,
                onValueChange = { firstPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                //password validation
                isError = firstPassword.length in 1..5 || !firstPassword.equals(secondPassword),
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth()
            )
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.password)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) }
            OutlinedTextField(
                value = secondPassword,
                onValueChange = { secondPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                //minimum length of the password
                isError = secondPassword.length in 1..5 || !firstPassword.equals(secondPassword),
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth()
            )

            // Button to sign up using email and password
            Button(
                onClick = {
                    if (firstPassword.isNotEmpty() && email.isNotEmpty() && !isEmailAlreadyUsed && secondPassword.equals(
                            secondPassword
                        )
                    ) { // Check if the email is not already used

                        scope.launch {
                            val methods = Firebase.auth.fetchSignInMethodsForEmail(email).await()
                            isEmailAlreadyUsed = methods.signInMethods!!.isNotEmpty()

                            if (isEmailAlreadyUsed){
                                infromUser(context,R.string.email_is_used)
                            }
                        }
                        scope.launch {
                            val result = viewModel.signUp(
                                email, firstPassword, userName
                            ) // Pass the user name to the signUp method
                            if (result != null) {
                                navController.navigate(Screens.SignInScreen.route)
                            } else {
                                infromUser(context, R.string.faild_to_signUp)
                            }
                        }
                    } else {
                        infromUser(
                            context, if (isEmailAlreadyUsed) R.string.email_is_used
                            else if (!secondPassword.equals(secondPassword)) R.string.passwords_not_same
                            else R.string.something_went_wrong
                        )
                    }
                },modifier = Modifier.fillMaxWidth().height(60.dp).padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueDarkMsafe
                ),
                shape = RoundedCornerShape(25),
            ) {
                changeLanguageViewModel.resourceHashMap.value?.get(R.string.sign_up)?.let {
                    Text(
                        it,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
    }
    //bottom light bar
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            drawRect(color = BlueVeryLightMsafe2)
        }
    }
}

fun infromUser(context: Context, msg: Int) {
    Toast.makeText(
        context, msg, Toast.LENGTH_SHORT
    ).show()
}





