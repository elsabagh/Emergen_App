package com.example.emergen_app.data.repository

import com.example.emergen_app.data.api.Path
import kotlinx.coroutines.flow.Flow

interface GraphHopperRepository {
    fun getRoute(start: String, end: String, apiKey: String): Flow<Result<Path>>
}
