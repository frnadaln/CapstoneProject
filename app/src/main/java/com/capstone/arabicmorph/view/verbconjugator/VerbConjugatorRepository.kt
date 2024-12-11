package com.capstone.arabicmorph.view.verbconjugator

import android.util.Log
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
            Log.d("Repository", "Calling API with verb: $verb, haraka: $haraka, transitive: $transitive")

            val response = apiService.getConjugation(verb, haraka, transitive)
            Log.d("Repository", "API Response: $response")
            Log.d("Repository", "Raw Response: $response")
            Log.d("Repository", "Suggest List: ${response.suggest}")

            if (response.suggest?.isNotEmpty() == true) {
                onResult(response)
            } else {
                Log.e("ConjugatorRepository", "No results found.")
                onResult(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ConjugatorRepository", "Error: ${e.message}")
            onResult(null)
        }
    }
}
