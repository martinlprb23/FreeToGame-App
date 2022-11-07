package com.mlr_apps.freetogame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mlr_apps.freetogame.ui.theme.FREETOGAMETheme
import com.mlr_apps.freetogame.views.GameInfo.GameInfoScreen
import com.mlr_apps.freetogame.views.GamesList.GamesListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //Important
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            if(isSystemInDarkTheme()){
                systemUiController.setNavigationBarColor(Color.Black, darkIcons = false)
                systemUiController.setStatusBarColor(Color.Black, darkIcons = false)
            }
            Screens()
        }
    }
}

@Composable
fun Screens() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "games_list"
    ){
        composable("games_list"){
            GamesListScreen(navController = navController)
        }

        composable(
            route = "Game_info/{gameId}",
            arguments = listOf(
                 navArgument("gameId"){type = NavType.IntType},
            )
        ){

            val gameId = remember {
                it.arguments?.getInt("gameId")
            }

            GameInfoScreen(
                gameId = gameId!!.toInt(),
                navController = navController
            )


        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FREETOGAMETheme {

    }
}