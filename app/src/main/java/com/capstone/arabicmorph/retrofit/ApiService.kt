package com.capstone.arabicmorph.retrofit

import com.capstone.arabicmorph.data.ConjugatorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("conjugator")
    suspend fun getConjugation(
        @Query("verb") verb: String,
        @Query("haraka") haraka: String = "u",
        @Query("transitive") transitive: Boolean = true
    ): ConjugatorResponse
}
