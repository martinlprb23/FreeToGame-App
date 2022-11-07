package com.mlr_apps.freetogame.data.Model

import com.mlr_apps.freetogame.data.remote.responses.Screenshot

data class GameInfoEntry(
    val title: String,
    val image: String,
    val description: String,
    val gameUrl: String,
    val images: Screenshot

)
