package com.example.msafe.ui.screens

sealed class Screens(val route: String) {
    object SignUpScreen: Screens("signUp_screen")

    object HomeScreen: Screens("home_screen1")
    object SignInScreen: Screens("signIn_screen")
    object QrScreen: Screens("qrCode_screen")
    object SuccessfulScreen: Screens("successful screen")
    object BiometricsScreen: Screens("biometrics screen")
    object SettingsScreen: Screens("settings screen")
    object ProfileScreen: Screens("profile screen")
    object PasswordResetScreen: Screens("reset screen")

    object QrInfoScreen: Screens("qrInfo screen")
    object HistoryScreen: Screens("history screen")

    object LanguageScreen: Screens("LanguageScreen")


}
