package com.example.msafe.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.msafe.api.Api
import com.example.msafe.util.Resource
import com.example.msafe.model.Data
import com.example.msafe.api.ApiService
import kotlinx.coroutines.withTimeout

class Repository {

    private val apiService: ApiService = Api.createApi()

    suspend fun getData(authorization: String, text: String, targetlanguage: String) : Resource<Data> {

        val response = try {
            withTimeout(5_000) {
                apiService.getTranslation(authorization, text, targetlanguage)
            }
        } catch(e: Exception) {
            Log.e("Repository", e.message ?: "No exception message available")
            return Resource.Error("An unknown error occured")
        }

        //Log.i(TAG, "THIS IS THE TRANSLATED TEXTTTTTTTTTTTTTT: " + response.text)

        return Resource.Success(response)
    }
}