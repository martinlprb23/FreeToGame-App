package com.mlr_apps.freetogame.data.remote

import com.mlr_apps.freetogame.data.remote.responses.GameInfo
import com.mlr_apps.freetogame.data.remote.responses.GamesList
import retrofit2.http.*

interface FreeToGameAPI {

    @GET("games")
    suspend fun getAllGames(
    ): GamesList

    @GET("game")
    suspend fun getGameInfo(
        @Query("id")gameId: Int
    ):GameInfo

}