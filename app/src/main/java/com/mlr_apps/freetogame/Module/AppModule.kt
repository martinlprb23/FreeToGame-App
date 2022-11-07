package com.mlr_apps.freetogame.Module

import com.mlr_apps.freetogame.data.remote.FreeToGameAPI
import com.mlr_apps.freetogame.repository.GamesRepository
import com.mlr_apps.freetogame.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun provideGamesRepository(
        api: FreeToGameAPI
    ) = GamesRepository(api)

    @Singleton
    @Provides
    fun provideGamesAPI(): FreeToGameAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(FreeToGameAPI::class.java)
    }
}