//https://flagdownload.com/

package com.example.msafe.model

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.msafe.R

class Translate {
    //method for putting all resource strings in one big string
    @Composable
    fun getAllResourceString(): String{
        val stringRes = R.string::class.java.fields
        var tempstring = ""

        for (field in stringRes) {
            if (field.type == Int::class.java) {
                val resId = field.getInt(null)
                val string = stringResource(id = resId)

                tempstring = "$tempstring / $resId * $string"
            }
        }

        return tempstring
    }

    //convert language string to language ID
    fun languageToId(name: String): String {
        when (name) {
            "English" -> return "EN"
            "Dutch" -> return "NL"
            "German" -> return "DE"
            "French" -> return "FR"
            "Spanish" -> return "ES"
            "Italian" -> return "IT"
            "Portugese" -> return "PT"
            "Danish" -> return "DA"
            "Norwegian" -> return "NB"
            "Swedish" -> return "SV"
            "Finnish" -> return "FI"
            "Greek" -> return "EL"
            "Ukrainian" -> return "UK"
            "Bulgarian" -> return "BG"
            "Hungarian" -> return "HU"
            "Polish" -> return "PL"
            "Turkish" -> return "TR"
            "Japanese" -> return "JA"
            "Korean" -> return "KO"
            else -> { // Note the block
                return "EN"
            }
        }
    }

    //convert language ID to language string
    fun idToLanguage(name: String): String {
        when (name) {
            "EN" -> return "English"
            "NL" -> return "Dutch"
            "DE" -> return "German"
            "FR" -> return "French"
            "ES" -> return "Spanish"
            "IT" -> return "Italian"
            "PT" -> return "Portugese"
            "DA" -> return "Danish"
            "NB" -> return "Norwegian"
            "SV" -> return "Swedish"
            "FI" -> return "Finnish"
            "EL" -> return "Greek"
            "UK" -> return "Ukrainian"
            "BG" -> return "Bulgarian"
            "HU" -> return "Hungarian"
            "PL" -> return "Polish"
            "TR" -> return "Turkish"
            "JA" -> return "Japanese"
            "KO" -> return "Korean"
            else -> { // Note the block
                return "English"
            }
        }
    }

    //convert language ID to country flag drawable int
    fun getResource(name: String): Int {
        when (name) {
            "EN" -> return R.drawable.unitedkingdom
            "NL" -> return R.drawable.netherlands
            "DE" -> return R.drawable.germany
            "FR" -> return R.drawable.france
            "ES" -> return R.drawable.spain
            "IT" -> return R.drawable.italy
            "PT" -> return R.drawable.portugal
            "DA" -> return R.drawable.denmark
            "NB" -> return R.drawable.norway
            "SV" -> return R.drawable.swedish
            "FI" -> return R.drawable.finland
            "EL" -> return R.drawable.greece
            "UK" -> return R.drawable.ukraine
            "BG" -> return R.drawable.bulgaria
            "HU" -> return R.drawable.hungary
            "PL" -> return R.drawable.poland
            "TR" -> return R.drawable.turkey
            "JA" -> return R.drawable.japan
            "KO" -> return R.drawable.korea
            else -> { // Note the block
                return R.drawable.unitedkingdom
            }
        }
    }
}