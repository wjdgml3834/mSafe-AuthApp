package com.example.msafe.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon

import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel

import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.example.msafe.util.Resource
import com.google.api.ResourceProto.resource

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QrInfoScreen(
    navController: NavHostController,
    viewModel: OneClickSIgnInViewModel,
    changeLanguageViewModel: ChangeLanguageViewModel
) {

    Scaffold(
        topBar = {
            Layout(navController, changeLanguageViewModel)
        },
        content = { infoScreen(changeLanguageViewModel) },
        bottomBar = {
            bottomInfoScreen(navController, changeLanguageViewModel)
        }
    )
}

@Composable
fun Layout(navController: NavController, changeLanguageViewModel: ChangeLanguageViewModel) {
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
            painter = painterResource(id = R.drawable.msafebig),
            contentDescription = "logo"
        )

        infoScreen(changeLanguageViewModel)

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
}

@Composable
fun infoScreen(changeLanguageViewModel: ChangeLanguageViewModel) {
    Column(modifier = Modifier.padding(20.dp, top = 50.dp, end = 20.dp)) {

        changeLanguageViewModel.resourceHashMap.value?.get(R.string.qr_code_title)?.let {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        //create a privacy declaration for the use of the camera
        changeLanguageViewModel.resourceHashMap.value?.get(R.string.dear_user)?.let {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = it
            )
        }

        changeLanguageViewModel.resourceHashMap.value?.get(R.string.qr_inform)?.let {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = it
            )
        }
        changeLanguageViewModel.resourceHashMap.value?.get(R.string.qr_inform_contact)?.let {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = it
            )
        }
    }
}


@Composable
fun bottomInfoScreen(navController: NavController, viewModel: ChangeLanguageViewModel) {
    //column for alignment
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueVeryLightMsafe2)
            .padding(top = 0.dp, bottom = 0.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            viewModel.resourceHashMap.value?.get(R.string.agree_terms_condition)?.let {
                Text(
                    modifier = Modifier.padding(top = 25.dp, start = 20.dp),
                    text = it,
                    color = Color.Black,
                    fontSize = 16.sp,
                )
            }

            TextButton(modifier = Modifier
                .width(90.dp)
                .padding(start = 40.dp, bottom = 15.dp, top = 15.dp)
                .height(50.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                onClick = {
                    navController.navigate(Screens.HomeScreen.route)
                }) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "continue",
                    Modifier.size(50.dp),
                    tint = Color.White
                )
            }


        }
    }
}