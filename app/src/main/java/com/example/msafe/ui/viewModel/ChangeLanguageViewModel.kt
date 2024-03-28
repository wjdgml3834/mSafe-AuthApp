package com.example.msafe.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msafe.model.Data
import com.example.msafe.repository.Repository
import com.example.msafe.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class ChangeLanguageViewModel : ViewModel() {
    val currentLanguage = mutableStateOf("")

    private val repository = Repository()

    //resource
    val resource: LiveData<Resource<Data>> get() = _resource
    private val _resource: MutableLiveData<Resource<Data>> = MutableLiveData(Resource.Empty())

    //language
    val language = mutableStateOf("EN")

    //retrieved cache
    val bigstring = mutableStateOf("")

    //hashmap for resources
    val resourceHashMap: LiveData<HashMap<Int, String>> get() = _resourceHashMap
    private val _resourceHashMap: MutableLiveData<HashMap<Int, String>> = MutableLiveData(hashMapOf())

    fun translateApp(targetLanguage: String, text: String) {
        _resource.value = Resource.Loading()

        viewModelScope.launch {
            _resource.value = repository.getData("DeepL-Auth-Key 60c382d7-7585-4fad-7fb9-4a819c4fab12:fx", text, targetLanguage)
        }
    }

    fun changeLanguage(context: Context, language: String) {
        val locale = when (language) {
            "nl" -> Locale("nl")
            "en" -> Locale.ENGLISH
            else -> context.resources.configuration.locale
        }

        val configuration = context.resources.configuration
        configuration.setLocale(locale)

        context.createConfigurationContext(configuration)
        currentLanguage.value = language
    }

    private val LANGUAGE_KEY = "language_key"

    fun saveLanguage(context: Context, language: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(LANGUAGE_KEY, language)
        editor.apply()
    }

    fun getSavedLanguage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_KEY, null)
    }
    fun getDeviceLanguage(context: Context): String {
        return Locale.getDefault().language
    }
}