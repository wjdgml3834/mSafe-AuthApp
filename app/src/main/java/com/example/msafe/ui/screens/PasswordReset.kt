package com.example.msafe.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.msafe.R
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PasswordResetScreen(navController: NavController, viewModel: OneClickSIgnInViewModel) {
    Scaffold(

    ) {
        PasswordReset(navController, viewModel)
    }
}

@Composable
fun PasswordReset(navController: NavController, viewModel: OneClickSIgnInViewModel) {
    val token = stringResource(R.string.your_web_client_id)
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
//
//    val launcher = viewModel.rememberFirebaseAuthLauncher(onAuthComplete = { result ->
//        viewModel.user = result.user
//        navController.navigate(Screens.HomeScreen1.route)
//    }, onAuthError = {
//        viewModel.user = null
//    })

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
            Text(stringResource(id = R.string.password_reset), style = MaterialTheme.typography.h4)

            Text(stringResource(id = R.string.email), style = MaterialTheme.typography.h6)
            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, modifier = Modifier.fillMaxWidth())

            // Button to sign up using email and password
            Button(
                onClick = {
                    viewModel.resetPassword(email)
                    email = ""
                    navController.navigate(Screens.SignInScreen.route)
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color(0xFF007BC5),
                )
            ) {
                Text(stringResource(id = R.string.password_reset), color = Color.White)
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







