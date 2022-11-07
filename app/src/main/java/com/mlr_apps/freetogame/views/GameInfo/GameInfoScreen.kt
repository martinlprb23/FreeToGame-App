package com.mlr_apps.freetogame.views.GameInfo

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Web
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.mlr_apps.freetogame.R
import com.mlr_apps.freetogame.data.remote.responses.GameInfo
import com.mlr_apps.freetogame.data.remote.responses.Screenshot
import com.mlr_apps.freetogame.ui.theme.FREETOGAMETheme
import com.mlr_apps.freetogame.ui.theme.RobotoCondensed
import com.mlr_apps.freetogame.ui.theme.backgroundThemeBlack
import com.mlr_apps.freetogame.utils.Resource
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameInfoScreen(
    gameId: Int,
    navController: NavController,
    viewModel: GameInfoViewModel = hiltViewModel()

) {
    FREETOGAMETheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val gameInfo = produceState<Resource<GameInfo>>(
                initialValue = Resource.Loading()
            ) {
                value = viewModel.getGameInfo(gameId = gameId.toInt())
            }.value

            if (gameInfo is Resource.Success) {
                gameInfo.data?.let { CollapsingImage(it, navController) }
            }

            if (gameInfo is Resource.Loading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class, ExperimentalToolbarApi::class)
@Composable
fun CollapsingImage(
    data: GameInfo,
    navController: NavController,
) {
    val painter = rememberImagePainter(data = data.thumbnail)
    val defaultDominantColor = MaterialTheme.colors.surface
    val state = rememberCollapsingToolbarScaffoldState()

    val webIntent: Intent = Uri.parse(data.freetogame_profile_url).let { webpage ->
        Intent(Intent.ACTION_VIEW, webpage)
    }

    val context = LocalContext.current

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            //val textSize = (21 + (31 - 21) * state.toolbarState.progress).sp
            Image(
                painter = painter,
                contentDescription = data.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .alpha(state.toolbarState.progress)
                    .road(Alignment.Center, Alignment.Center),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIos,
                            contentDescription = "Icon back screen",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 60.dp, end = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.title,
                        modifier = Modifier
                            .alpha(1 - state.toolbarState.progress),
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = { context.startActivity(webIntent) },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.OpenInBrowser,
                            contentDescription = "Icon web page",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }

            }

        }

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //INFO GAME
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = data.genre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )

                            Text(
                                text = "Genre",
                                fontSize = 12.sp,
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = data.platform,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(text = "Platform", fontSize = 12.sp)
                        }
                    }

                    Column(Modifier.padding(top = 24.dp)) {
                        Text(
                            text = data.publisher,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = "Publisher", fontSize = 12.sp,
                        )
                    }

                    Column(Modifier.padding(top = 16.dp, bottom = 24.dp)) {
                        Text(
                            text = data.developer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = "Developer",
                            fontSize = 12.sp,
                        )
                    }

                    Text(
                        text = "About ${data.title}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.description,
                        fontSize = 14.sp
                    )
                }
            }

            //SCREENSHOTS
            item {
                Text(
                    text = "Screenshots",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(data.screenshots.size) {

                        val url = data.screenshots.map { it.image }
                        Log.d("IMG", url.toString())
                        val painterScreenshot = rememberImagePainter(data = url[it])
                        val painterState = painterScreenshot.state

                        Card(
                            modifier = Modifier
                                .width(300.dp)
                                .height(200.dp),
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Image(
                                painter = painterScreenshot,
                                contentDescription = data.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { },
                                contentScale = ContentScale.Crop
                            )

                            if (painterState is ImagePainter.State.Loading) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colors.primary,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(24.dp),
                                        strokeWidth = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}