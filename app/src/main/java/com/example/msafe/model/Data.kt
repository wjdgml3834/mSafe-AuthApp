package com.example.msafe.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("translations") val entiretext: List<Any>
)