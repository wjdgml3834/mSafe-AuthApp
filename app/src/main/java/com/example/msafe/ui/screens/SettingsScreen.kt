package com.example.msafe

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import kotlinx.coroutines.launch
import com.deepl.api.*
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.ui.screens.Screens
import com.example.msafe.ui.screens.TextToSpeechCreator
import com.example.msafe.ui.theme.*
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.util.Resource
import com.google.api.ResourceProto.resource
import java.io.File
import java.io.FileOutputStream

class SettingsScreen {
    @RequiresApi(Build.VERSION_CODES.N)
    @Composable
    fun SettingsScreenComp(
        navController: NavHostController,
        viewModel: ChangeLanguageViewModel,
        modifier: Modifier,
        context: Context,
        oneClickSIgnInViewModel: OneClickSIgnInViewModel
    ) {
        LayoutSettingsScreen(navController = navController, context = context, viewModel = viewModel, oneClickSIgnInViewModel)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Composable
    fun LayoutSettingsScreen(navController: NavHostController, context: Context, viewModel: ChangeLanguageViewModel, oneClickSIgnInViewModel: OneClickSIgnInViewModel) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "My shared message")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        val resource by viewModel.resource.observeAsState()

        //storage
        val externalFilesDir = context.getExternalFilesDir(null)
        val file = File(externalFilesDir, "translation.txt")
        val outputStream = FileOutputStream(file)
        outputStream.write(resource?.data.toString().toByteArray())
        outputStream.close()

        val textToSpeech = TextToSpeechCreator()
        textToSpeech.initialize()

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


            Column(modifier = Modifier
                .width(350.dp)
                .height(530.dp)
                .padding(top = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        SettingColor,
                        shape = RoundedCornerShape(3)
                    ))
                {
                    //title
                    viewModel.resourceHashMap.value?.get(R.string.setting_title)?.let {
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 45.dp), textAlign = TextAlign.Center, fontSize = 20.sp, text = it
                        )
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
                                viewModel.resourceHashMap.value?.set(resId, array[0])
                            }
                        }
                    }


                    //language setting
                    TextButton(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 180.dp, start = 30.dp, end = 30.dp)
                        .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        onClick = {
                            navController.navigate(Screens.LanguageScreen.route)
                        })
                    {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Language, contentDescription = "language", Modifier.size(30.dp), tint = Color.Black)
                            viewModel.resourceHashMap.value?.get(R.string.language)
                                ?.let { Text(modifier = Modifier.fillMaxWidth().padding(top = 3.dp, start = 15.dp), fontSize = 15.sp, textAlign = TextAlign.Left, text = it) }
                        }
                    }

                    //history setting
                    TextButton(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 250.dp, start = 30.dp, end = 30.dp)
                        .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        onClick = {
                            navController.navigate(Screens.HistoryScreen.route)
                        })
                    {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = "History",
                                Modifier.size(30.dp),
                                tint = Color.Black
                            )
                            viewModel.resourceHashMap.value?.get(R.string.history)?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 3.dp, start = 15.dp),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Left,
                                    text = it
                                )
                            }
                        }
                    }

                    //get link setting
                    TextButton(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 320.dp, start = 30.dp, end = 30.dp)
                        .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        onClick = {
                            viewModel.viewModelScope.launch {
                                oneClickSIgnInViewModel.sendLinkEmail()
                                Toast.makeText(
                                    context,
                                    "Email sent. Please check your inbox.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Default.Mail,
                                contentDescription = "Mail",
                                Modifier.size(30.dp),
                                tint = Color.Black
                            )
                            viewModel.resourceHashMap.value?.get(R.string.get_link)?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 3.dp, start = 15.dp),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Left,
                                    //text = Translate().getString(resource, R.string.get_link)
                                    text = it
                                )
                            }
                        }
                    }

                    //profile setting
                    TextButton(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 110.dp, start = 30.dp, end = 30.dp)
                        .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        onClick = {
                            navController.navigate(Screens.ProfileScreen.route)
                        })
                    {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "profile",
                                Modifier.size(30.dp),
                                tint = Color.Black
                            )
                            viewModel.resourceHashMap.value?.get(R.string.profile)?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 3.dp, start = 15.dp),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Left,
                                    //text = Translate().getString(resource, R.string.profile)
                                    text = it
                                )
                            }
                        }
                    }

                    //share setting
                    TextButton(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 390.dp, start = 30.dp, end = 30.dp)
                        .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        onClick = {
                            context.startActivity(shareIntent)
                        })
                    {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "share",
                                Modifier.size(30.dp),
                                tint = Color.Black
                            )
                            viewModel.resourceHashMap.value?.get(R.string.share)?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 3.dp, start = 15.dp),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Left,
                                    //text = Translate().getString(resource, R.string.share)
                                    text = it
                                )
                            }
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
                        navController.navigate(Screens.HomeScreen.route)
                    }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back", Modifier.size(50.dp), tint = Color.White)
                }

                val text =  arrayOf(
                    stringResource(id = R.string.settings_1),
                    stringResource(id = R.string.explanation),
                    stringResource(id = R.string.settings_2),
                    stringResource(id = R.string.settings_option_1),
                    stringResource(id = R.string.settings_option_2),
                    stringResource(id = R.string.settings_option_3),
                    stringResource(id = R.string.settings_option_4),)

                TextButton(modifier = Modifier
                    .width(65.dp)
                    .padding(start = 15.dp, bottom = 15.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                    onClick = {
                        textToSpeech.speak(text)
                    }) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Translate", Modifier.size(50.dp), tint = Color.White)
                }

                //draw flag
                FloatingActionButton(modifier = Modifier
                    .padding(start = 220.dp, top = 3.dp)
                    .size(45.dp),
                    backgroundColor = Color.Transparent,
                    onClick = {
                        navController.navigate(Screens.LanguageScreen.route)
                    }) {
                    Image(
                        painterResource(Translate().getResource(viewModel.language.value)),
                        "flag"
                    )
                }
            }
        }
    }
}