package com.example.emergen_app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GraphHopperApiService {
    @GET("route")
    suspend fun getRoute(
        @Query("key") apiKey: String,
        @Query("point") start: String,
        @Query("point") end: String,
        @Query("vehicle") vehicle: String = "car",
        @Query("locale") locale: String = "en",
        @Query("calc_points") calcPoints: Boolean = false
    ): GraphHopperResponse
}
