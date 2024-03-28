package com.example.msafe.ui.theme.screens

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.example.msafe.R
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import com.example.msafe.database.Information
import com.example.msafe.ui.screens.*
import com.example.msafe.ui.theme.BlueDarkMsafe
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import com.example.msafe.util.Resource
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class HomeScreen {
    @Composable
    fun HomeScreenComp(
        navController: NavHostController,
        viewModel: ChangeLanguageViewModel,
        oneClickSIgnInViewModel: OneClickSIgnInViewModel,
        modifier: Modifier,
        context: FragmentActivity
    ) {
        if (oneClickSIgnInViewModel.user!=null) {
            Layout(context = context, navController = navController, oneClickSIgnInViewModel = oneClickSIgnInViewModel, viewModel)
        }
    }

    @Composable
    fun Layout(context: FragmentActivity, navController: NavHostController, oneClickSIgnInViewModel: OneClickSIgnInViewModel, viewModelLanguage : ChangeLanguageViewModel) {
        var exitState by remember { mutableStateOf(false) }
        var exitStateApiPopup by remember { mutableStateOf(false) }
        val textToSpeech =  TextToSpeechCreator()

        textToSpeech.initialize()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "MSAFE HEUUUU")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        val resource by viewModelLanguage.resource.observeAsState()
        val externalFilesDir = context.getExternalFilesDir(null)

        //retrieve cached language
        val file2 = File(externalFilesDir, "language.txt")
        if (file2.exists()) {
            //retrieve cache
            val inputStream2 = FileInputStream(file2)
            viewModelLanguage.language.value = inputStream2.bufferedReader().use { it.readText() }
        }else{
            //write EN to language.txt and save to viewmodel
            val outputStream2 = FileOutputStream(file2)
            outputStream2.write("EN".toByteArray())
            outputStream2.close()
            viewModelLanguage.language.value = "EN"
        }

        //retrieve storage string
        val file = File(externalFilesDir, "translation.txt")

        //check if the file exists to avoid exception
        if (file.exists()) {
            val inputStream = FileInputStream(file)

            //retrieve cache
            viewModelLanguage.bigstring.value = inputStream.bufferedReader().use { it.readText() }

            //check if bigstring is filled
            if (viewModelLanguage.bigstring.value.length > 100) {
                val lengthResourceInt = 13
                val stringRes = R.string::class.java.fields

                //add cached string to hashmap for the app
                for (field in stringRes) {
                    if (field.type == Int::class.java) {
                        val resId = field.getInt(null)
                        val index = viewModelLanguage.bigstring.value.indexOf(resId.toString())
                        val newString =
                            viewModelLanguage.bigstring.value.substring(index + lengthResourceInt)
                        val array = newString.split('/')
                        viewModelLanguage.resourceHashMap.value?.set(resId, array[0])
                    }
                }
            }

            inputStream.close()
        }

        //hashmap not filled yet
        if (viewModelLanguage.resourceHashMap.value?.get(R.string.welcome) == null) {
            val stringRes = R.string::class.java.fields
            for (field in stringRes) {
                if (field.type == Int::class.java) {
                    val resId = field.getInt(null)
                    val string = stringResource(id = resId)

                    viewModelLanguage.resourceHashMap.value?.set(resId, string)
                }
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
            //text under logo
            Text(
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 12.sp,
                text = "Authenticator App",
                color = BlueDarkMsafe
            )

            //text
            viewModelLanguage.resourceHashMap.value?.get(R.string.welcome)?.let { Text(modifier = Modifier.padding(top = 50.dp), fontSize = 26.sp, text = it) }
            Text(modifier = Modifier.padding(top = 20.dp), fontStyle = FontStyle.Italic, fontSize = 16.sp, text = viewModelLanguage.resourceHashMap.value?.get(R.string.logged_in_user) +  oneClickSIgnInViewModel.user!!.displayName.toString())

            //qr code pic
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.padding(top = 70.dp, end = 10.dp),
                    painter = painterResource(id = R.drawable.qrcode),
                    contentDescription = "QR-code"
                )
                Image(
                    modifier = Modifier.padding(top = 70.dp, end = 10.dp),
                    painter = painterResource(id = R.drawable.fingerprintsmall),
                    contentDescription = "Fingerprint"
                )
                Image(
                    modifier = Modifier.padding(top = 70.dp),
                    painter = painterResource(id = R.drawable.facerecsmall),
                    contentDescription = "Facerec"
                )
            }

            viewModelLanguage.resourceHashMap.value?.get(R.string.click_below_the_scan)?.let { Text(modifier = Modifier.padding(top = 70.dp), fontSize = 16.sp, text = it) }
            viewModelLanguage.resourceHashMap.value?.get(R.string.qr_computer_screen)?.let { Text(modifier = Modifier.padding(top = 10.dp), fontSize = 16.sp, text = it) }

            //button
            Button(modifier = Modifier
                .width(175.dp)
                .height(135.dp)
                .padding(top = 83.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueDarkMsafe,
                    contentColor = Color.White
                ),
                onClick = {
                    navController.navigate(Screens.QrScreen.route)
                    //exitStateApiPopup = true
                }) {
                viewModelLanguage.resourceHashMap.value?.get(R.string.scan_qr_code)?.let { Text(text = it) }
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
                        exitState = true
                        //initiate the instance ${Information}
                        val information = Information(false)
                        //update the value of the boolean in the database so an action will be taken based on its value
                        updateScannedValue(information, oneClickSIgnInViewModel)
                        //add the log out activity to database
                        addLoggedInToHistory(information,oneClickSIgnInViewModel)
                    }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Exit", Modifier.size(50.dp), tint = Color.White)
                }


                val text =  arrayOf(
                    stringResource(id = R.string.homeScreen_1),
                    stringResource(id = R.string.homeScreen_2),
                    oneClickSIgnInViewModel.user!!.displayName.toString(),
                    stringResource(id = R.string.homeScreen_3))

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

                TextButton(modifier = Modifier
                    .width(265.dp)
                    .padding(start = 215.dp, bottom = 15.dp)
                    .height(50.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(backgroundColor = BlueLightMsafe),
                    onClick = {
                        navController.navigate(Screens.SettingsScreen.route)
                    }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", Modifier.size(50.dp), tint = Color.White)
                }
            }
        }
        if (exitState) {
            PushNotification(navController = navController, viewModelLanguage, resource, context = context){
                exitState = false
            }
        }
        if (exitStateApiPopup){
            PincodePopupScreen(navController = navController, viewModelLanguage, resource) {
                exitStateApiPopup = false
            }
        }
    }

    @Composable
    fun PincodePopupScreen(navController: NavHostController, viewModel: ChangeLanguageViewModel, resource: Resource<Data>?, onDismiss: () -> Unit) {
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

                        viewModel.resourceHashMap.value?.get(R.string.instruction_pincode)?.let { Text(fontSize = 15.sp, text = it) }
                        viewModel.resourceHashMap.value?.get(R.string.computer_screen)?.let { Text(fontSize = 15.sp, text = it) }

                        Spacer(modifier = Modifier.height(40.dp))

                        viewModel.resourceHashMap.value?.get(R.string.user_pincode)?.let { Text(fontSize = 15.sp, text = it) }

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
                                navController.navigate(Screens.BiometricsScreen.route)
                                onDismiss()
                            }) {
                            viewModel.resourceHashMap.value?.get(R.string.confirm)?.let {
                                Text(
                                    text = it,
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

    @Composable
    fun PushNotification(navController: NavHostController, viewModel: ChangeLanguageViewModel, resource: Resource<Data>?, context: FragmentActivity, onDismiss: () -> Unit) {
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

                    viewModel.resourceHashMap.value?.get(R.string.exit_confirm)?.let {
                        Text(
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            text = it,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                    }

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
                            viewModel.resourceHashMap.value?.get(R.string.confirm_yes)?.let {
                                Text(
                                    text = it,
                                    color = Color.White,
                                    style = TextStyle(
                                        fontSize = 16.sp
                                    )
                                )
                            }
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
                            viewModel.resourceHashMap.value?.get(R.string.confirm_no)?.let {
                                Text(
                                    text = it,
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
}