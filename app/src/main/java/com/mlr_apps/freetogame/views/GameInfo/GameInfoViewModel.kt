package com.mlr_apps.freetogame.views.GameInfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.mlr_apps.freetogame.data.remote.responses.GameInfo
import com.mlr_apps.freetogame.repository.GamesRepository
import com.mlr_apps.freetogame.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameInfoViewModel
@Inject
constructor(
    private val repository: GamesRepository
    ): ViewModel() {
    suspend fun getGameInfo(gameId: Int): Resource<GameInfo>{
        return repository.getGameInfo(gameId)
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) ->Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate{ pallete ->
            pallete?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}