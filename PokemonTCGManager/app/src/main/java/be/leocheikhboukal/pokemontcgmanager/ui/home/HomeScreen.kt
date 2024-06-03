package be.leocheikhboukal.pokemontcgmanager.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
            .padding(horizontal = 68.dp, vertical = 150.dp),
    ){
        Column(
            modifier = Modifier
                .background(Color(252, 61, 61))
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
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
            val image = when(user.color) {
                1 -> painterResource(R.drawable.red)
                2 -> painterResource(R.drawable.blue)
                3 -> painterResource(R.drawable.yellow)
                4 -> painterResource(R.drawable.purple)
                else -> {
                    painterResource(R.drawable.ic_broken_image)
                }
            }

            Image(painter = image, contentDescription = null, modifier = Modifier)

            Text(text = user.name)


        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
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
                        User(id = 0, name = "Leptar", color = 1),
                        User(id = 0, name = "Odonata", color = 2),
                        User(id = 0, name = "LemonSqueezer", color = 3),
                        User(id = 0, name = "Naomie", color = 4)),
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
    PokemonTCGManagerTheme {
        UserProfileCard(
            User(id = 0, name = "Leptar", color = 1),
            modifier = Modifier
        )
    }
}