/*
author: Jill Jessurun
date: march 2023

sources:
- https://github.com/fvilarino/Jetpack-Biometric-Sample
- https://semicolonspace.com/jetpack-compose-dialog-2fa/
 */

package com.example.msafe

import HistoryScreen
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.msafe.ui.screens.*
import com.example.msafe.ui.theme.MSafeTheme
import com.example.msafe.ui.screens.BiometricsScreen
import com.example.msafe.ui.theme.SplashScreen
import com.example.msafe.ui.theme.screens.HomeScreen
import com.example.msafe.ui.theme.screens.ProfileScreen
import com.example.msafe.ui.theme.screens.SuccessfulScreen
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import com.example.msafe.ui.viewModel.OneClickSIgnInViewModel

class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MSafeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SplashScreen(onFinish = {
                        navigateToNextScreen()
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun navigateToNextScreen() {
        setContent {
            val navController = rememberNavController()
            // Replace HomeScreenTest with the actual name of your composable function
            mSafeNavHost(navController = navController)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Composable
    fun mSafeNavHost(
        viewModel: OneClickSIgnInViewModel = viewModel(),
        navController: NavHostController,
        changeLanguageViewModel: ChangeLanguageViewModel = viewModel(),
        modifier: Modifier = Modifier
    ) {

        val context = LocalContext.current
        NavHost(
            navController = navController,
            startDestination = if (viewModel.user!= null) Screens.HomeScreen.route else Screens.SignInScreen.route,
            modifier = modifier
        ) {
            composable(Screens.QrInfoScreen.route) {
                QrInfoScreen(navController = navController, viewModel, changeLanguageViewModel)
            }

            composable(Screens.QrScreen.route) {
                QrScreen(navController = navController, changeLanguageViewModel, viewModel)
            }

            composable(Screens.SignInScreen.route) {
                SignInScreen(navController = navController, viewModel, changeLanguageViewModel, this@MainActivity)
            }
            composable(Screens.SignUpScreen.route) {
                SignUpScreen(navController = navController, viewModel, changeLanguageViewModel)
            }
            composable(route = Screens.HomeScreen.route) {
                HomeScreen().HomeScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    viewModel,
                    modifier,
                    this@MainActivity
                )
            }
            composable(Screens.SignUpScreen.route) {
                SignUpScreen(navController = navController, viewModel, changeLanguageViewModel)
            }
            composable(route = Screens.BiometricsScreen.route) {
                BiometricsScreen().BiometricsScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    modifier,
                    this@MainActivity
                )
            }
            composable(Screens.SuccessfulScreen.route) {
                SuccessfulScreen().SuccessfulScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    modifier,
                    context
                )
            }
            composable(Screens.SettingsScreen.route) {
                SettingsScreen().SettingsScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    modifier,
                    context,
                    oneClickSIgnInViewModel = viewModel
                )
            }
            composable(Screens.ProfileScreen.route) {
                ProfileScreen().ProfileScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    viewModel,
                    modifier,
                    context
                )
            }
            composable(Screens.HomeScreen.route) {
                HomeScreen().HomeScreenComp(
                    navController = navController,
                    changeLanguageViewModel,
                    viewModel,
                    modifier,
                    this@MainActivity
                )
            }
            composable(Screens.PasswordResetScreen.route) {
                PasswordResetScreen(
                    navController = navController, viewModel = viewModel
                )
            }
            composable(Screens.QrScreen.route) {
                QrScreen(navController = navController, changeLanguageViewModel, viewModel)
            }
            composable(Screens.LanguageScreen.route) {
                LanguageScreen().LanguageScreenComp(navController = navController, changeLanguageViewModel, modifier, context, viewModel)
            }
            composable(Screens.HistoryScreen.route) {
                HistoryScreen(navController = navController, viewModel, changeLanguageViewModel)
            }
        }
    }
}






