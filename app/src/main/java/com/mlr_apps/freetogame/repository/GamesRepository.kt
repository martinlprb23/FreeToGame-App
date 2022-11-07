package com.mlr_apps.freetogame.repository

import android.util.Log
import com.mlr_apps.freetogame.data.remote.FreeToGameAPI
import com.mlr_apps.freetogame.data.remote.responses.GameInfo
import com.mlr_apps.freetogame.data.remote.responses.GamesList
import com.mlr_apps.freetogame.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class GamesRepository @Inject constructor(
    private val api: FreeToGameAPI
) {
    suspend fun getGamesList(): Resource<GamesList>{
        val response = try {
            api.getAllGames()
        }catch (e: Exception){
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getGameInfo(gameId: Int):Resource<GameInfo>{
        val response = try {
            Log.d("Id_xd", gameId.toString())
            api.getGameInfo(gameId = gameId)
        }catch (e: Exception){
            Log.d("ErroXD", e.toString())
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

}