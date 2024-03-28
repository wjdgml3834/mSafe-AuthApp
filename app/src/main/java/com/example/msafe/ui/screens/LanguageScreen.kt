package com.example.msafe.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class LanguageScreen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun LanguageScreenComp(
        navController: NavHostController,
        viewModel: ChangeLanguageViewModel,
        modifier: Modifier,
        context: Context,
        oneClickSIgnInViewModel: OneClickSIgnInViewModel
    ) {
        val resource by viewModel.resource.observeAsState()
        val resourceHashmap by viewModel.resourceHashMap.observeAsState()
        var currentlanguage by remember { mutableStateOf(Translate().idToLanguage(viewModel.language.value)) }
        var exit by remember { mutableStateOf(false) }
        var drawLoadingScreen by remember { mutableStateOf(false) }

        val languages = HashMap<String, Int>()
        languages["English"] = R.drawable.unitedkingdom
        languages["Dutch"] = R.drawable.netherlands
        languages["German"] = R.drawable.germany
        languages["French"] = R.drawable.france
        languages["Spanish"] = R.drawable.spain
        languages["Italian"] = R.drawable.italy
        languages["Portugese"] = R.drawable.portugal
        languages["Danish"] = R.drawable.denmark
        languages["Norwegian"] = R.drawable.norway
        languages["Swedish"] = R.drawable.swedish
        languages["Finnish"] = R.drawable.finland
        languages["Greek"] = R.drawable.greece
        languages["Ukrainian"] = R.drawable.ukraine
        languages["Bulgarian"] = R.drawable.bulgaria
        languages["Hungarian"] = R.drawable.hungary
        languages["Polish"] = R.drawable.poland
        languages["Turkish"] = R.drawable.turkey
        languages["Japanese"] = R.drawable.japan
        languages["Korean"] = R.drawable.korea

        //display list languages
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier
                    .align(Alignment.TopStart)
                    .padding(top = 150.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                items(
                    items = languages.keys.sorted(),
                    itemContent = { language ->
                        TextButton(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                            .height(90.dp),
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            onClick = {
                                currentlanguage = language
                            }) {
                            Row(
                                modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(color = BlueVeryLightMsafe)
                            ) {
                                //draw flag
                                languages[language]?.let { painterResource(id = it) }?.let {
                                    Image(
                                        modifier = Modifier.padding(
                                            start = 12.dp,
                                            top = 12.dp,
                                            end = 40.dp
                                        ),
                                        painter = it,
                                        contentDescription = "flag"
                                    )
                                }
                                //text flag
                                Text(
                                    text = language,
                                    modifier = Modifier.padding(top = 23.dp),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    })
            }
        }

        //title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding()
        ) {
            viewModel.resourceHashMap.value?.get(R.string.select_a_language)?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 110.dp)
                        .align(Alignment.Center),
                    text = it,
                    fontSize = 20.sp
                )
            }
        }

        //top light bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 780.dp)
        ) {
            drawRect(color = BlueVeryLightMsafe)
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

        //back button
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
                        if (oneClickSIgnInViewModel.user!= null) navController.navigate(Screens.SettingsScreen.route) else navController.navigate(Screens.SignInScreen.route)
                    }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "back",
                        Modifier.size(50.dp),
                        tint = Color.White
                    )
                }

                //Text(modifier = Modifier.padding(start = 80.dp, top = 12.dp),text = "Selected =  ")
                languages[currentlanguage]?.let { painterResource(id = it) }?.let {
                    Image(
                        modifier = Modifier
                            .padding(start = 220.dp, top = 5.dp)
                            .size(40.dp),
                        painter = it,
                        contentDescription = "flag"
                    )
                }

                TextButton(modifier = Modifier
                    .width(70.dp)
                    .padding(start = 20.dp, bottom = 15.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                    onClick = {
                        exit = true
                    }) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "confirm",
                        Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }
        }

        //scope for wait function
        val scope = rememberCoroutineScope()

        if (exit) {
            //set chosen language by user
            viewModel.language.value = Translate().languageToId(currentlanguage)

            //cache the language
            val externalFilesDir = context.getExternalFilesDir(null)
            val file = File(externalFilesDir, "language.txt")
            val outputStream = FileOutputStream(file)
            outputStream.write(viewModel.language.value.toByteArray())
            outputStream.close()

            //translate app strings
            viewModel.translateApp(viewModel.language.value, Translate().getAllResourceString())

            //wait for the api to do his work
            scope.launch {
                waitForApi()
                //navController.navigate(Screens.SettingsScreen.route)
                if (oneClickSIgnInViewModel.user!= null) navController.navigate(Screens.SettingsScreen.route) else navController.navigate(Screens.SignInScreen.route)
            }

            //Log.i(TAG, "" + resource?.data.toString())

            //redirect to settings screen
            exit = false
        }
    }

    suspend fun waitForApi(): Boolean {
        delay(1500)
        return true
    }
}