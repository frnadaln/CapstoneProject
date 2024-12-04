package com.capstone.arabicmorph.api

import com.capstone.arabicmorph.data.JamidResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    suspend fun predictJamid(
        @Body request: Map<String, String>
    ): JamidResponse
}
