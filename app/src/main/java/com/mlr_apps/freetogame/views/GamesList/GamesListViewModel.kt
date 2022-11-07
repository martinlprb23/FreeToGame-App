package com.mlr_apps.freetogame.views.GamesList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.mlr_apps.freetogame.data.Model.GamesListEntry
import com.mlr_apps.freetogame.repository.GamesRepository
import com.mlr_apps.freetogame.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GamesListViewModel @Inject constructor(
    private val repository: GamesRepository
): ViewModel() {

    var gamesList =  mutableStateOf<List<GamesListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadGames()
    }

    fun loadGames() {

        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getGamesList()
            when (result){
                is Resource.Success ->{
                    Log.d("Nice", result.data!!.get(1).thumbnail)
                    val gamesEntries = result.data.mapIndexed{i, entry ->
                        val id = entry.id
                        val url = entry.thumbnail
                        val description = entry.short_description
                        val genre = entry.genre
                        val platform = entry.platform

                        GamesListEntry(entry.title.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, url, description, genre, platform,id)
                    }
                    loadError.value = ""
                    isLoading.value = false
                    gamesList.value += gamesEntries
                }
                is Resource.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
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