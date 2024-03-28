package com.example.msafe.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel

import com.example.msafe.util.Resource

class SuccessfulScreen {
    @Composable
    fun SuccessfulScreenComp(navController: NavHostController, viewModel: ChangeLanguageViewModel, modifier: Modifier, context: Context){
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

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(top = 100.dp),
                painter = painterResource(id = R.drawable.msafe),
                contentDescription = "logo"
            )
            Text(modifier = Modifier.padding(top = 10.dp), fontSize = 12.sp, text = "Authenticator App", color = BlueDarkMsafe)

            viewModel.resourceHashMap.value?.get(R.string.welcome_suc)
                ?.let { Text(modifier = Modifier.padding(top = 123.dp), fontSize = 25.sp, text = it) }

            Image(
                modifier = Modifier.padding(top = 50.dp),
                painter = painterResource(id = R.drawable.check),
                contentDescription = "check"
            )

            //button
            Button(modifier = Modifier
                .width(174.dp)
                .height(195.dp)
                .padding(top = 145.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = BlueDarkMsafe, contentColor = Color.White),
                onClick = {
                    navController.navigate(Screens.HomeScreen.route)
                }) {
                viewModel.resourceHashMap.value?.get(R.string.home_suc)?.let { Text(text = it) }
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
        }
    }
}