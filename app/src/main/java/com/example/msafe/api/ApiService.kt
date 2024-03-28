package com.example.msafe.api

import com.example.msafe.model.Data
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("https://api-free.deepl.com/v2/translate")
    suspend fun getTranslation(
        @Header("Authorization") authorization: String,
        @Query("text") text: String,
        @Query("target_lang") targetlanguage: String
    ): Data
}