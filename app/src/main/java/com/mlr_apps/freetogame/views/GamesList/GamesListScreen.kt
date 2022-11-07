package com.mlr_apps.freetogame.views.GamesList

import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.* //
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.mlr_apps.freetogame.R
import com.mlr_apps.freetogame.data.Model.GamesListEntry
import com.mlr_apps.freetogame.ui.theme.FREETOGAMETheme
import com.mlr_apps.freetogame.ui.theme.RobotoCondensed


@Composable
fun GamesListScreen(
    navController: NavController
) {
    FREETOGAMETheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(Modifier.fillMaxSize()) {
                TopBar()
                GamesList(navController)
            }
        }
    }
}

@Composable
fun GamesList(
    navController: NavController,
    viewModel: GamesListViewModel = hiltViewModel()
) {
    val gamesList by remember{viewModel.gamesList}
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 20.dp, end = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ){
        items(gamesList.size){
            if (!isLoading){
                LaunchedEffect(key1 = true ){
                    viewModel.loadGames()
                }
            }
            GameEntry(gamesList[it], navController)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }

        if (loadError.isNotEmpty()){
           /*TODO*/
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameEntry(
    entry: GamesListEntry,
    navController: NavController,
    viewModel: GamesListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember{ mutableStateOf(defaultDominantColor) }

    val painter = rememberImagePainter(data = entry.imageUrl)
    val painterState = painter.state
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .height(300.dp)
            .background(color = MaterialTheme.colors.onPrimary)
            .clickable {
                navController.navigate(
                    route = "Game_info/${entry.number}"
                )
            }, contentAlignment = Alignment.TopCenter
    ){
        Column {

            Box{
                Image(
                    painter = painter,
                    contentDescription = entry.gameName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                )

                if (painterState is ImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Center)
                    )
                }else if(painterState is ImagePainter.State.Success){
                    LaunchedEffect(key1 = painter){
                        val image = painter.imageLoader.execute(painter.request).drawable
                        viewModel.calcDominantColor(image!!) {
                           dominantColor = it
                        }
                    }
                }
            }

            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = entry.gameName,
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF3989D8))

                ){
                    Text(
                        text = " FREE ",
                        fontFamily = RobotoCondensed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                        //color = Color(0xFFFFFFFF)
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = entry.description,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = entry.genre +" for " +entry.platform,
                    fontSize = 12.sp,
                    color = Color(0xFF868686)
                )
            }

        }
    }

}

@Composable
fun TopBar() {
    TopAppBar(
        title = {

            Image(
                painter = painterResource(id = R.drawable.freetogame),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(125.dp)
                    .height(50.dp)
            ) },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Ver más")
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}