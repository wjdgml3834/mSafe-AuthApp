package com.example.msafe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = BlueDarkMsafe,
    primaryVariant = BlueLightMsafe,
    secondary = Teal200 ,
    background = BackgroundWhite

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


private val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Black,
    secondary = Teal200,
    background = BackgroundWhite

)


@Composable
fun MSafeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = BackgroundBlack
    )

    //LightColorPalette
    val colors = if (darkTheme) {
        //DarkColorPalette
        LightColorPalette
    } else {
        LightColorPalette
    }



    //LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}