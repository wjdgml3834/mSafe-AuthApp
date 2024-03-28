package com.example.msafe.ui.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.screens.Screens
import com.example.msafe.ui.theme.BlueDarkMsafe
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.example.msafe.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.api.ResourceProto.resource

class ProfileScreen {
    @Composable
    fun ProfileScreenComp(
        navController: NavHostController,
        viewModel: ChangeLanguageViewModel,
        oneClickSIgnInViewModel: OneClickSIgnInViewModel,
        modifier: Modifier,
        context: Context
    ) {
        LayoutProfileScreen(navController = navController, context = context, viewModel, oneClickSIgnInViewModel)
    }

    @Composable
    fun LayoutProfileScreen(navController: NavHostController, context: Context, viewModel: ChangeLanguageViewModel, oneClickSIgnInViewModel: OneClickSIgnInViewModel) {
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
            Text(
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 12.sp,
                text = "Authenticator App",
                color = BlueDarkMsafe
            )

            //text
            //Text(modifier = Modifier.padding(top = 80.dp), fontSize = 20.sp, text ="hoi" + " mSafeEmployee32")
            //Text(modifier = Modifier.padding(top = 80.dp), fontSize = 20.sp, text = Translate().getString(resource, R.string.user) + " " + oneClickSIgnInViewModel.user!!.displayName.toString())

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                if (oneClickSIgnInViewModel.user != null){
                    Text(modifier = Modifier.padding(start = 40.dp, top = 12.dp), fontSize = 14.sp, text = oneClickSIgnInViewModel.user!!.displayName.toString())
                    Text(modifier = Modifier.padding(start = 40.dp, top = 12.dp), fontSize = 14.sp, text = oneClickSIgnInViewModel.user!!.email.toString())
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
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                drawRect(color = BlueVeryLightMsafe2)
            }
        }

        //back button
        //column for alignment
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(modifier = Modifier
                    .width(65.dp)
                    .padding(start = 15.dp, bottom = 15.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                    onClick = {
                        navController.navigate(Screens.SettingsScreen.route)
                    }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "back",
                        Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
                TextButton(modifier = Modifier
                    .width(230.dp)
                    .padding(start = 150.dp, bottom = 15.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                    onClick = {
                        Toast.makeText(
                            context,
                            "Edit",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    viewModel.resourceHashMap.value?.get(R.string.edit)
                        ?.let { Text(color = Color.White, text = it) }
                }
                TextButton(modifier = Modifier
                    .width(230.dp)
                    .padding(start = 10.dp, bottom = 15.dp, end = 10.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe),
                    onClick = {
                        Firebase.auth.signOut()
                        oneClickSIgnInViewModel.user = null
                        navController.navigate(Screens.SignInScreen.route)

                        Toast.makeText(
                            context,
                            "Logged out",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    viewModel.resourceHashMap.value?.get(R.string.logout)
                        ?.let { Text(color = Color.White, text = it) }
                }
            }
        }
    }
}