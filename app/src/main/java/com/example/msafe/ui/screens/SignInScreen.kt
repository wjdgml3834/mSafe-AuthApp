package com.example.msafe.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.localizedStringResource
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.theme.BlueDarkMsafe
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.example.msafe.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.ResourceProto.resource
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInScreen(navController: NavController, viewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel, context: FragmentActivity) {
    var exitState by remember { mutableStateOf(false) }
    val resource by changeLanguageViewModel.resource.observeAsState()

    //fill hashmap in viewmodel
    if (changeLanguageViewModel.resourceHashMap.value?.get(R.string.welcome) == null) {
        val stringRes = R.string::class.java.fields
        for (field in stringRes) {
            if (field.type == Int::class.java) {
                val resId = field.getInt(null)
                val string = stringResource(id = resId)

                changeLanguageViewModel.resourceHashMap.value?.set(resId, string)
            }
        }
    }

    //divide strings and add to resourceHashMap (after translating the app the resource in viewmodel changed, so hashmap needs to be updated)
    if (resource?.data.toString().length > 4) {
        val lengthResourceInt = 13

        val stringRes = R.string::class.java.fields

        for (field in stringRes) {
            if (field.type == Int::class.java) {
                val resId = field.getInt(null)
                //val string = stringResource(id = resId)

                val index = resource?.data.toString().indexOf(resId.toString())
                val newString = resource?.data.toString().substring(index + lengthResourceInt)
                val array = newString.split('/')
                changeLanguageViewModel.resourceHashMap.value?.set(resId, array[0])
            }
        }

        //storage
        val externalFilesDir = context.getExternalFilesDir(null)
        val file = File(externalFilesDir, "translation.txt")
        val outputStream = FileOutputStream(file)
        outputStream.write(resource?.data.toString().toByteArray())
        outputStream.close()
    }

    val externalFilesDir = context.getExternalFilesDir(null)

    //retrieve cached language
    val file2 = File(externalFilesDir, "language.txt")
    if (file2.exists()) {
        //retrieve cache
        val inputStream2 = FileInputStream(file2)
        changeLanguageViewModel.language.value = inputStream2.bufferedReader().use { it.readText() }
        inputStream2.close()
    }else{
        //write EN to language.txt and save to viewmodel
        val outputStream2 = FileOutputStream(file2)
        outputStream2.write("EN".toByteArray())
        outputStream2.close()
        changeLanguageViewModel.language.value = "EN"
    }

    //retrieve cached bigstring
    val file = File(externalFilesDir, "translation.txt")
    if (file.exists()) {
        val inputStream = FileInputStream(file)
        val tempstring = inputStream.bufferedReader().use { it.readText() }
        changeLanguageViewModel.bigstring.value = tempstring

        //check if cached data is a translated bigstring
        if (changeLanguageViewModel.bigstring.value.length > 50){
            //changeLanguageViewModel.bigstring.value = inputStream.bufferedReader().use { it.readText() }

            val lengthResourceInt = 13
            val stringRes = R.string::class.java.fields

            //add cached string to hashmap for the app
            for (field in stringRes) {
                if (field.type == Int::class.java) {
                    val resId = field.getInt(null)
                    val index = changeLanguageViewModel.bigstring.value.indexOf(resId.toString())
                    val newString = changeLanguageViewModel.bigstring.value.substring(index + lengthResourceInt)
                    val array = newString.split('/')
                    changeLanguageViewModel.resourceHashMap.value?.set(resId, array[0])
                }
            }
        }else{
            //fill bigstring in viewmodel
            if (changeLanguageViewModel.resourceHashMap.value?.get(R.string.welcome) == null) {
                val stringRes = R.string::class.java.fields
                for (field in stringRes) {
                    if (field.type == Int::class.java) {
                        val resId = field.getInt(null)
                        val string = stringResource(id = resId)

                        changeLanguageViewModel.resourceHashMap.value?.set(resId, string)
                    }
                }
            }
        }
    }

    Scaffold(

    ) {
        AlertDialog(changeLanguageViewModel, context)
        signIn(navController, viewModel,changeLanguageViewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 770.dp),
            horizontalArrangement = Arrangement.End,

            ) {
            TextButton(modifier = Modifier
                .width(250.dp)
                .padding(end = 200.dp, bottom = 15.dp)
                .height(50.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                onClick = {
                    exitState = true
                }) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "quit",
                    Modifier.size(50.dp),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }

            if (exitState) {
                PushNotification(navController = navController, context = context){
                    exitState = false
                }
            }

            //draw flag
            FloatingActionButton(modifier = Modifier
                .padding(start = 85.dp, top = 2.dp, end = 15.dp)
                .size(45.dp),
                backgroundColor = Color.Transparent,
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
fun AlertDialog(changeLanguageViewModel: ChangeLanguageViewModel, context: Context) {
    val savedLanguage = changeLanguageViewModel.getSavedLanguage(context)

    val supportedLanguages = listOf("en", "nl")
    val deviceLanguage = changeLanguageViewModel.getDeviceLanguage(context).takeIf { it in supportedLanguages } ?: "en"

    if (deviceLanguage !in supportedLanguages) {
        changeLanguageViewModel.changeLanguage(context, "en")
        changeLanguageViewModel.saveLanguage(context, "en")
    }

    if (savedLanguage != null) {
        changeLanguageViewModel.changeLanguage(context, savedLanguage)
    }

    // State to control visibility of AlertDialog
    val showDialog = remember { mutableStateOf(savedLanguage != null && savedLanguage != deviceLanguage) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Change language") },
            text = { Text("Would you like to switch to the language in the app?") },
            confirmButton = {
                Button(
                    onClick = {
                        changeLanguageViewModel.changeLanguage(context, deviceLanguage)
                        changeLanguageViewModel.saveLanguage(context, deviceLanguage)
                        showDialog.value = false // Dismiss AlertDialog
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueDarkMsafe,
                        contentColor = Color.White
                    )
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }, // Dismiss AlertDialog
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueDarkMsafe,
                        contentColor = Color.White
                    )
                )
                {
                    Text("No")
                }
            }
        )
    }
    if (savedLanguage != null) {
        changeLanguageViewModel.changeLanguage(context, savedLanguage)
    }
}

@Composable
fun signIn(navController: NavController, viewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel) {
    val token = stringResource(R.string.your_web_client_id)
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val launcher = viewModel.rememberFirebaseAuthLauncher(onAuthComplete = { result ->
        viewModel.user = result.user
        navController.navigate(Screens.QrInfoScreen.route)
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
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.logged_in_user)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 20.dp)) }
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
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
                    changeLanguageViewModel.resourceHashMap.value?.get(R.string.sign_in)?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(start = 15.dp),
                            color = Color.White
                        )
                    }
                }
            }
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.email)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 10.dp)) }
            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(25))
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.password)
                ?.let { Text(it, fontSize = 18.sp, modifier = Modifier.padding(bottom = 5.dp, top = 10.dp)) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                //password validation
                isError = password.length in 1..5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25)
            )
            // Button to sign up using email and password
            Button(
                onClick = {
                    if (password.isNotEmpty() && email.isNotEmpty()) { // Check if the email is not already used
                        scope.launch {
                            val result = viewModel.signInWithEmailAndPassword(
                                email, password
                            )
                            if (result != null) {
                                viewModel.user = result.user
                                navController.navigate(Screens.QrInfoScreen.route)
                            } else {
                                infromUser(context, R.string.faild_to_signUp)
                            }
                        }
                    } else {
                        infromUser(
                            context, R.string.something_went_wrong
                        )
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 5.dp)
                    .height(40.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueDarkMsafe
                ),
                shape = RoundedCornerShape(25)
            ) {
                changeLanguageViewModel.resourceHashMap.value?.get(R.string.sign_in)
            }

            changeLanguageViewModel.resourceHashMap.value?.get(R.string.go_to_sign_up)?.let {
                Text(
                    text = it, modifier = Modifier.clickable {
                        navController.navigate(Screens.SignUpScreen.route)
                    }, textDecoration = TextDecoration.Underline, color = Color(0xFF24547C)
                )
            }

            changeLanguageViewModel.resourceHashMap.value?.get(R.string.password_reset)?.let {
                Text(
                    text = it, modifier = Modifier.clickable {
                        scope.launch {
                            navController.navigate(Screens.PasswordResetScreen.route)
                        }
                    }, textDecoration = TextDecoration.Underline, color = Color(0xFF24547C)
                )
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

@Composable
fun PushNotification(navController: NavController, context: FragmentActivity, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismiss
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(color = Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp),
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = "2-Step Verification",
                        alignment = Alignment.Center
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    text = "Do you want to exit?",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )

                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe),
                        onClick = {
                            onDismiss()
                            context.finish()
                            System.exit(0)
                        }) {
                        Text(
                            text = "Yes",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                    }

                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe),
                        onClick = {
                            onDismiss()
                            //context.finish()
                            //System.exit(0)
                        }) {
                        Text(
                            text = "No",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}







