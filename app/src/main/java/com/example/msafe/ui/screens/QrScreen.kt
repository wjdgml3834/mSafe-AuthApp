package com.example.msafe.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.msafe.R
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.msafe.model.Data
import com.example.msafe.model.Translate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msafe.database.Information
import com.example.msafe.ui.theme.BlueLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe
import com.example.msafe.ui.theme.BlueVeryLightMsafe2
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.util.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

private var bottomText by mutableStateOf("Please scan the QR code")
private var successIcon by mutableStateOf(Icons.Filled.Search)
private var successIconColor by mutableStateOf(Color.Black)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QrScreen(
    navController: NavController,
    viewModel: ChangeLanguageViewModel,
    oneClickSIgnInViewModel: OneClickSIgnInViewModel
) {
    Scaffold(

        content = { cameraScreen(navController, viewModel, oneClickSIgnInViewModel) },

        )
}

@Composable
fun cameraScreen(
    navController: NavController,
    viewModel: ChangeLanguageViewModel,
    oneClickSIgnInViewModel: OneClickSIgnInViewModel
) {
    val resource by viewModel.resource.observeAsState()
    //bottomText = Translate().getString(resource, R.string.please_scan_qr_code)
    bottomText = "hoi"
    var scanned by remember {
        mutableStateOf(false)
    }

    Column {
        var code by remember {
            mutableStateOf("")
        }
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }
        var hasCamPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCamPermission = granted
            }
        )
        LaunchedEffect(key1 = true) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        Column(

        ) {
            if (hasCamPermission) {

                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                code = result
                                //Check code here
                                if (code == "https://try.msafe.nl/") {
                                    //important to navigate directly other wise it will scan the QR code more than one time which causes bugs
                                    navController.navigate(Screens.BiometricsScreen.route)
                                    bottomText = "QR-code successfully detected!"
                                    successIcon = Icons.Filled.Check
                                    successIconColor = Color.Green
                                    Thread.sleep(1000)
                                    //creat the instanse with true value
                                    scanned = true
                                    val infomation = Information(scanned)
                                    //update the boolean in the database so action will be taken based on that
                                    updateScannedValue(
                                        infomation,
                                        oneClickSIgnInViewModel
                                    )
                                    infromUser(context, R.string.sucessfully_scanned)
                                    //add to data base that the user has logged in
                                    addLoggedInToHistory(infomation,oneClickSIgnInViewModel)

                                } else if (code != "https://try.msafe.nl/") {
                                    infromUser(context, R.string.wrong_qr)
                                }
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
//                            println("Error: ${e.message}")
//                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**

 *Updates the scanned value in the Firestore database for a specific user.
 * @param information The Information object containing the scanned value.
 * @param viewModel The OneClickSIgnInViewModel instance.
 */
fun updateScannedValue(
    information: Information,
    viewModel: OneClickSIgnInViewModel
) {
    val db = Firebase.firestore
    val collection = db.collection("QrCollection")
    val userId = viewModel.user?.uid
    val info = hashMapOf(
        "scanned" to information.scanned,
        "time" to Calendar.getInstance().time.toString()
    )

    collection.document(userId.toString())
        .set(info) // unique collection its ID the same as the user ID
        .addOnSuccessListener {
            println("Data updated successfully")
        }
        .addOnFailureListener { e ->
            println("Error updating data: $e")
        }
}

/**
 *Adds a logged-in entry to the history in the Firestore database.
 *@param information The Information object containing the logged-in information.
 */
fun addLoggedInToHistory(
    information: Information,
    oneClickSIgnInViewModel: OneClickSIgnInViewModel
) {

    val db = Firebase.firestore
    val collection = db.collection("log_in_out_history")
    val historyElement = hashMapOf(
        "time" to Calendar.getInstance().time.toString(),
        "action" to information.scanned,
        "userId" to oneClickSIgnInViewModel.user?.uid
    )
    collection.add(historyElement)

}
