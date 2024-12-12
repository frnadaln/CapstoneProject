package com.capstone.arabicmorph.view.verbconjugator

import com.capstone.arabicmorph.data.ConjugatorResponse
import com.capstone.arabicmorph.retrofit.ApiConfig

class ConjugatorRepository {

    suspend fun getConjugationResults(
        verb: String,
        haraka: String = "u",
        transitive: Int = 1,
        onResult: (ConjugatorResponse?) -> Unit
    ) {
        try {
            val apiService = ApiConfig.getApiService()
            val response = apiService.getConjugation(verb, haraka, transitive)

            if (response.suggest?.isNotEmpty() == true) {
                onResult(response)
            } else {
                onResult(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null)
        }
    }
}
