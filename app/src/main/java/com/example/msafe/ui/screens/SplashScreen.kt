package com.example.msafe.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.msafe.R
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(true) {
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 1000))
        delay(2000) // Wait for 2 seconds
        onFinish() // Call onFinish callback to move to main screen
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center,

            ){
            AnimatedVisibility(
                visible = alpha.value > 0,
                modifier = Modifier.alpha(alpha.value)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.msafesuperbig),
                    contentDescription = "Logo",

                    )
            }
        }
    }
}