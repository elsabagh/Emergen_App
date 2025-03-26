package com.example.emergen_app.di

import android.app.Application
import android.content.Context
import com.example.emergen_app.data.api.GraphHopperApiService
import com.example.emergen_app.data.repository.GraphHopperRepository
import com.example.emergen_app.data.repository.GraphHopperRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://graphhopper.com/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideGraphHopperApiService(retrofit: Retrofit): GraphHopperApiService {
        return retrofit.create(GraphHopperApiService::class.java)
    }

    @Provides
    fun provideGraphHopperRepository(api: GraphHopperApiService): GraphHopperRepository {
        return GraphHopperRepositoryImpl(api)
    }

}