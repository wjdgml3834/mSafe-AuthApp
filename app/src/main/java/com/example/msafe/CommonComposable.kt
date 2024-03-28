package com.example.msafe

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import java.util.*

@Composable
fun localizedStringResource(@StringRes id: Int, changeLanguageViewModel: ChangeLanguageViewModel): String {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val locale = when (changeLanguageViewModel.currentLanguage.value) {
        "nl" -> Locale("nl")
        "en" -> Locale.ENGLISH
        else -> configuration.locale
    }

    val localizedConfiguration = Configuration(configuration).apply {
        setLocale(locale)
    }

    return context.createConfigurationContext(localizedConfiguration).getString(id)
}