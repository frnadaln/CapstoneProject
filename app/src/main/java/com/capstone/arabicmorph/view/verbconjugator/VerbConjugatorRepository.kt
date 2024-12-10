package com.capstone.arabicmorph.data

import com.capstone.arabicmorph.retrofit.ApiConfig
import retrofit2.Response

class ConjugatorRepository {

    suspend fun getConjugationResults(verb: String, haraka: String = "u", transitive: Boolean = true): Response<ConjugatorResponse> {
        return ApiConfig.apiService.getConjugation(verb, haraka, transitive)
    }
}