package com.capstone.arabicmorph.retrofit

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "http://qutrub.arabeyes.org/api/"

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        Log.d("API Config", "ApiService created: $apiService")
        return apiService
    }
}

