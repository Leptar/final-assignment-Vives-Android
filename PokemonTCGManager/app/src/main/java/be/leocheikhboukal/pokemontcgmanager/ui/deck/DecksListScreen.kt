package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme

object DecksListDestination : NavigationDestination {
    override val route = "decks_list"
    const val USER_ID_ARG = "userId"
    val routeWithArgs = "$route/{$USER_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksListScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false,
    navigateToDeck: (Int) -> Unit,
    navigateToDeckCreate: (Int) -> Unit,
    navigateToCardSearch: () -> Unit,
    navigateToUser: (Int) -> Unit,
    viewModel: DecksListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState = viewModel.stateFlowUiState.collectAsState()


    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(252, 61, 61)),
        topBar = {
            PTCGManagerTitleAppBar(
                scrollBehavior = scrollBehavior,
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        containerColor = Color(252,61,61),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            PTCGManagerSubAppBar(
                userId = uiState.value.user.id,
                navigateToCardSearch = navigateToCardSearch,
                navigateToDecksList = navigateToDeck,
                navigateToProfile = navigateToUser
            )
            DecksListBody(
                modifier = Modifier,
                uiState = uiState.value,
                onDeckClick = navigateToDeck,
                onCreateDeckClick = navigateToDeckCreate
            )
        }

    }
}

@Composable
fun DecksListBody(
    modifier: Modifier = Modifier,
    onDeckClick: (Int) -> Unit,
    onCreateDeckClick: (Int) -> Unit,
    uiState: UserWithDecksUiState
) {

    Column(
        modifier = modifier
            .background(Color(252, 61, 61)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row {
            Text(
                text = "${uiState.user.name}'s Decks",
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .weight(1f)
        ){
            LazyVerticalGrid(
                modifier = Modifier
                    .background(Color(252, 61, 61))
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .fillMaxSize(),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Top
            ) {
                if(uiState.decks.isEmpty()) {
                    item(span = { GridItemSpan(3) }) {
                        Text(
                            text = "You have no Decks... Kind of sad",
                            color = Color.LightGray,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                } else {
                    items(uiState.decks) { deck ->
                        DecksCard(
                            deck = deck,
                            onClick = onDeckClick
                        )
                    }
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ){
            Button(
                onClick = {onCreateDeckClick(uiState.user.id)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(117,255,114),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color.Black),
                content = {
                    Text(
                        text = "Create Deck",
                        fontSize = 24.sp
                    )
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksCard(
    modifier: Modifier = Modifier,
    deck: Deck,
    onClick: (Int) -> Unit
) {
    Card (
        onClick = { onClick(deck.id) },
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
            val image = when(deck.category) {
                1 -> painterResource(R.drawable.red)
                2 -> painterResource(R.drawable.blue)
                3 -> painterResource(R.drawable.yellow)
                4 -> painterResource(R.drawable.purple)
                else -> {
                    painterResource(R.drawable.ic_broken_image)
                }
            }

            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
            )

            Text(
                text = deck.name,
                fontSize = 12.sp
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DecksListBodyPreview() {
    PokemonTCGManagerTheme {
        DecksListBody(
            onCreateDeckClick = {},
            onDeckClick = {},
            uiState = UserWithDecksUiState(
                User(1, "Test", 1),
                listOf(
                    Deck(1, "Fun", "deck non competitif, pour les copaing", listOf(), 1,1),
                    Deck(2, "Try hard", "volonté de nuire", listOf(), 2, 1),
                    Deck(3, "Fun", "deck non competitif, pour les copaing", listOf(), 3,1),
                    Deck(4, "Try hard", "volonté de nuire", listOf(), 4, 1)
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DecksListBodyEmptyPreview() {
    PokemonTCGManagerTheme {
        DecksListBody(
            onDeckClick = {},
            onCreateDeckClick = {},
            uiState = UserWithDecksUiState(
                User(1, "Test", 1),
                listOf()
            )
        )
    }
}