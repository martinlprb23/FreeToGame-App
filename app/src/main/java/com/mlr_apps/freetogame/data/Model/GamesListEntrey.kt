package com.mlr_apps.freetogame.data.Model

data class GamesListEntry(
    val gameName : String,
    val imageUrl : String,
    val description: String,
    val genre: String,
    val platform: String,
    val number : Int
)
