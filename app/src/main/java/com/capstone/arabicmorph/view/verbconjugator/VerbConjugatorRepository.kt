package com.capstone.arabicmorph.view.verbconjugator

import com.capstone.arabicmorph.data.ConjugatorResponse
import com.capstone.arabicmorph.retrofit.ApiConfig
import retrofit2.Response

class ConjugatorRepository {

    suspend fun getConjugationResults(verb: String): Response<ConjugatorResponse> {
        return ApiConfig.apiService.getConjugation(verb)
    }
}