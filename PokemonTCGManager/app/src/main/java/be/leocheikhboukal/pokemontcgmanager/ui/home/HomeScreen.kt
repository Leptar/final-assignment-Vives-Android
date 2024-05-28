package be.leocheikhboukal.pokemontcgmanager.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import java.io.File

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(252, 61, 61)),
        topBar = {
            PTCGManagerTitleAppBar(
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color(252,61,61),
    ) { innerPadding ->
        HomeBody(
            userList = homeUiState.userList,
            contentPadding = innerPadding,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeBody(
    userList: List<User>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 68.dp, vertical = 150.dp)
            .background(Color.Red)
            .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
    ){
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 2.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select your Profile",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 5.dp)
            )


            if(userList.isNotEmpty()) {
                LazyVerticalGrid (
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    items(userList) { user ->
                        UserProfileCard(
                            user = user,
                            modifier = modifier
                        )
                    }
                }
            } else {
                Text(
                    text = "No one is here...",
                    color = Color.LightGray
                )
            }

            Box(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
            Text(
                text = "First connection or new member ?",
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = {  },
                modifier = Modifier
            ) {
                Text(
                    text = "Create a new profile !",
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileCard(user: User, modifier: Modifier) {
    Card (
        onClick = { },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .padding(13.dp)
    ) {
        Column (
           verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (user.data != null) {
                val bmp = BitmapFactory.decodeByteArray(
                    user.data, 0, user.data!!.size
                ).asImageBitmap()

                Image(
                    bitmap = bmp,
                    contentDescription = "",
                    modifier = modifier.size(90.dp)
                )

            } else {
                Image(
                    painter = painterResource(R.drawable.title_default_image),
                    contentDescription = "Default Image",
                    modifier = modifier.size(90.dp)
                )
            }

            Text(text = user.name)


        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    val file = File("D:\\Cours\\Vives\\Android\\Final_assignment\\images.png")
    PokemonTCGManagerTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            topBar = {
                PTCGManagerTitleAppBar(
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                HomeBody(
                    userList = listOf(
                        User(id = 0, name = "Leptar", data = file.readBytes()),
                        User(id = 0, name = "Leptar", data = file.readBytes()),
                        User(id = 0, name = "Leptar", data = file.readBytes()),
                        User(id = 0, name = "Leptar", data = file.readBytes())),
                    contentPadding = innerPadding,
                )
            },
            containerColor = Color(252,61,61),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),

        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    PokemonTCGManagerTheme {
        HomeBody(listOf())
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    val file = File("D:\\Cours\\Vives\\Android\\Final_assignment\\images.png")
    PokemonTCGManagerTheme {
        UserProfileCard(
            User(id = 0, name = "Leptar", data = file.readBytes() ),
            modifier = Modifier
        )
    }
}