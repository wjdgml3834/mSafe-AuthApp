import android.annotation.SuppressLint
import android.content.ClipData.Item
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.msafe.R
import com.example.msafe.database.LogInHistory
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
import com.google.api.ResourceProto.resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.security.auth.login.LoginException


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HistoryScreen(navController: NavController, viewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel) {
    Scaffold(

    ) {
        History(navController, viewModel, changeLanguageViewModel)
    }
}


@Composable
fun History(navController: NavController, oneClickSIgnInViewModel: OneClickSIgnInViewModel, changeLanguageViewModel: ChangeLanguageViewModel) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("log_in_out_history")
    var historyList by remember { mutableStateOf(emptyList<LogInHistory>()) }


    collectionRef.get()
        .addOnSuccessListener { querySnapshot: QuerySnapshot ->
            // Handle the query snapshot here
            val list = mutableListOf<LogInHistory>()
            for (document in querySnapshot) {
                var time = document.get("time").toString()
                println("11111111111111111")
                println(time)
                var action = document.get("action").toString()
                var userId = document.get("userId")
                var history = LogInHistory(time, action)
                if (userId == oneClickSIgnInViewModel.user?.uid) {
                    list.add(history)

                }
            }
            historyList = list
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp),

            verticalArrangement = Arrangement.Center

        ) {
            historyList.forEach { item: LogInHistory ->
                item {
                    itemFiled(item, changeLanguageViewModel)
                    Spacer(modifier = Modifier.height(40.dp))

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

    //back button
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
                navController.navigate(Screens.SettingsScreen.route)
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

//list to show the log in history can be modified and style
@Composable
fun itemFiled(
    logInHistory: LogInHistory,
    viewModel: ChangeLanguageViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 5.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = logInHistory.time,
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (logInHistory.loggedIn == "true") {
                    viewModel.resourceHashMap.value?.get(R.string.userLoggedIn)?.let { Text(modifier = Modifier.padding(top = 10.dp), text = it) }
                } else {
                    viewModel.resourceHashMap.value?.get(R.string.userLoggedOut)?.let { Text(modifier = Modifier.padding(top = 10.dp), text = it) }
                }
            }
        }
    }
    //Divider(color = Color.LightGray, thickness = 1.dp)
}







