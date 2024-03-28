package com.example.msafe.ui.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.msafe.ui.viewModel.ChangeLanguageViewModel
import java.util.*

 class TextToSpeechCreator() {
   private var textToSpeech:TextToSpeech? = null
   private var viewModelLanguage = ChangeLanguageViewModel()


    @Composable
    fun initialize(){

        var context = LocalContext.current


        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            textToSpeech?.setSpeechRate(0.8f)


            when (status) {
                TextToSpeech.SUCCESS -> {
                    // TextToSpeech initialization successful
                    textToSpeech?.language = Locale(viewModelLanguage.currentLanguage.value)

                }
                TextToSpeech.ERROR -> {
                    // TextToSpeech encountered an error during initialization
                    Log.e("TTS error:", "Initialization failed: check your TextToSpeech settings")

                }
                TextToSpeech.ERROR_NETWORK -> {
                    // TextToSpeech network operation failed
                    Log.e("TTS error:", "Network error: check internet connection")
                }
                TextToSpeech.LANG_NOT_SUPPORTED -> {
                    // TextToSpeech language is not supported
                    Log.e("TTS error:", "Language not supported: check your TextToSpeech settings")
                    Log.e( "TTS error:", "Language not supported: Default language set to English")
                    textToSpeech?.language = Locale("en")

                }

                else -> {
                    // TextToSpeech unknown error
                    Log.e("TTS error:", "Initialization failed: unknown error")
                    Log.e("TTS error:", "Restart app if problem persists")

                }


            }
        })
    }

    fun speak(text: Array<String>) {
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID")

        for (i in text.indices) {
            val delay = 500L // Delay between parts except the first one
            textToSpeech!!.playSilentUtterance(delay, TextToSpeech.QUEUE_ADD, "UniqueID")
            textToSpeech!!.speak(text[i].toString(), TextToSpeech.QUEUE_ADD, params, "UniqueID")
        }

    }

    fun stop() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }



}