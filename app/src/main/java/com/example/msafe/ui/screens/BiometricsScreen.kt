package com.example.msafe.ui.screens

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.theme.*
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.example.msafe.util.Resource

//error codes
private val biometricsIgnoredErrors = listOf(
    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_USER_CANCELED,
    BiometricPrompt.ERROR_NO_BIOMETRICS
)

class BiometricsScreen() {
    @Composable
    fun BiometricsScreenComp(
        navController: NavHostController,
        viewModel: ChangeLanguageViewModel,
        modifier: Modifier,
        context: FragmentActivity
    ) {
        AskForBiometrics(context, navController, viewModel)
    }

    @Composable
    fun AskForBiometrics(context: FragmentActivity, navController: NavHostController, viewModel: ChangeLanguageViewModel) {
        var openDialog by remember { mutableStateOf(false) }
        var showPrompt by remember { mutableStateOf(true) }
        val resource by viewModel.resource.observeAsState()

        //top light bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 780.dp)
        ) {
            drawRect(color = BlueVeryLightMsafe)
        }

        //column for alignment
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //logo
            Image(
                modifier = Modifier.padding(top = 100.dp),
                painter = painterResource(id = R.drawable.msafe),
                contentDescription = "logo"
            )
            //text under logo
            Text(
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 12.sp,
                text = "Authenticator App",
                color = BlueDarkMsafe
            )

            //info text
            viewModel.resourceHashMap.value?.get(R.string.biometric_required)?.let {
                Text(
                    modifier = Modifier.padding(top = 90.dp),
                    fontSize = 18.sp,
                    text = it
                )
            }
            viewModel.resourceHashMap.value?.get(R.string.instruction_biometric)?.let {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = it
                )
            }

            //row for horizontal alignment icons
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                //icons
                Image(
                    painter = painterResource(id = R.drawable.fingerprint),
                    contentDescription = "fingerprint"
                )
                Image(
                    modifier = Modifier.padding(start = 20.dp),
                    painter = painterResource(id = R.drawable.facerec),
                    contentDescription = "facerec"
                )
            }

            //button
            Button(modifier = Modifier
                .width(174.dp)
                .height(180.dp)
                .padding(top = 130.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueDarkMsafe,
                    contentColor = Color.White
                ),
                onClick = {
                    showPrompt = true
                }) {
                viewModel.resourceHashMap.value?.get(R.string.authenticate)?.let { Text(text = it) }
            }

            //check if device has biometrics
            if (showPrompt) {
                if (biometricsAvailable(context)) {
                    //show the biometrics
                    showBiometricPrompt(context, navController, viewModel)
                } else {
                    openDialog = true
                }
                showPrompt = false
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

        if (openDialog) {
            PincodePopupScreen(navController = navController) {
                openDialog = false
            }
        }

        //column for alignment
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            TextButton(modifier = Modifier
                .width(65.dp)
                .padding(start = 15.dp, bottom = 15.dp)
                .height(50.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                onClick = {
                    navController.navigate(Screens.HomeScreen.route)
                }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "back",
                    Modifier.size(50.dp),
                    tint = Color.White
                )
            }
        }
    }

    @Composable
    private fun showBiometricPrompt(context: FragmentActivity, navController: NavHostController, changeLanguageViewModel: ChangeLanguageViewModel) {
        val promptInfo =
            changeLanguageViewModel.resourceHashMap.value?.get(R.string.biometric_required)?.let {
                BiometricPrompt.PromptInfo.Builder()
                    //.setTitle("Biometric authentication required")
                    .setTitle(it)
                    .setSubtitle(changeLanguageViewModel.resourceHashMap.value?.get(R.string.log_in_using_your_biometric_credential))
                    .setNegativeButtonText(changeLanguageViewModel.resourceHashMap.value?.get(R.string.back)!!)
                    .build()
            }

        val biometricPrompt = BiometricPrompt(
            context,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    if (errorCode !in biometricsIgnoredErrors) {
                        Toast.makeText(
                            context,
                            "Authentication failed " + errString,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    navController.navigate(Screens.SuccessfulScreen.route)
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        context,
                        "Authentication failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
        if (promptInfo != null) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun biometricsAvailable(context: FragmentActivity): Boolean {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                return false
            }
        }
        return false
    }

    /*
    @Composable
    fun PushNotification(navController: NavHostController, onDismiss: () -> Unit) {
        Dialog(
            onDismissRequest = {
                onDismiss()
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
                        text = "No biometrics available",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 20.sp
                        )
                    )

                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        text = "Click below to confirm your login",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp
                        )
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, start = 36.dp, end = 36.dp, bottom = 25.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe),
                        onClick = {
                            onDismiss()
                            navController.navigate(Screens.SuccessfulScreen.route)
                        }) {
                        Text(
                            text = "Confirm",
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

     */

    @Composable
    fun PincodePopupScreen(navController: NavHostController, onDismiss: () -> Unit) {
        val pinCode = (1000..9999).shuffled().last()

        Dialog(
            onDismissRequest = { /* nothing happens when the dialog is dismissed */ }
        ) {
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(color = Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(fontSize = 15.sp, text = "Please enter the pincode on your")
                        Text(fontSize = 15.sp, text = "computer screen")

                        Spacer(modifier = Modifier.height(40.dp))

                        Text(fontSize = 15.sp, text = "Your pincode: ")

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(fontSize = 20.sp, text = "" + pinCode)

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            modifier = Modifier
                                .width(175.dp)
                                .height(135.dp)
                                .padding(top = 20.dp, bottom = 16.dp),
                            shape = RoundedCornerShape(25),
                            colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe),
                            onClick = {
                                navController.navigate(Screens.SuccessfulScreen.route)
                                onDismiss()
                            }) {
                            Text(
                                text = "Confirm",
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
}