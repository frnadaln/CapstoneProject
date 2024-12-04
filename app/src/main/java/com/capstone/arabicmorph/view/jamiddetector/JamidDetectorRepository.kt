package com.capstone.arabicmorph.view.jamiddetector

import com.capstone.arabicmorph.api.ApiConfig
import com.capstone.arabicmorph.data.JamidResponse

class JamidDetectorRepository {
    private val apiService = ApiConfig.getApiService()

    suspend fun predictJamid(input: String): Result<JamidResponse> {
        return try {
            val request = mapOf("text" to input)
            val response = apiService.predictJamid(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
