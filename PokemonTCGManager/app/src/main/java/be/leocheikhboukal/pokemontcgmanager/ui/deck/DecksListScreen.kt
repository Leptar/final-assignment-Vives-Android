package be.leocheikhboukal.pokemontcgmanager.ui.deck

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.rememberCoroutineScope
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
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.profile.ProfileAddBody
import be.leocheikhboukal.pokemontcgmanager.ui.profile.ProfileAddUiState
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import kotlinx.coroutines.flow.StateFlow

object DecksListDestination : NavigationDestination {
    override val route = "DecksList"
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksListScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false,
    viewModel: DecksListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
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
        DecksListBody(
            modifier = Modifier,
            uiState = uiState.value,
            contentPadding = innerPadding,
            onCreateDeckClick = {}
        )
    }
}

@Composable
fun DecksListBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onCreateDeckClick: () -> Unit,
    uiState: UserWithDecksUiState
) {

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 68.dp, vertical = 150.dp)
            .background(Color(252, 61, 61)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${uiState.user.name}'s Decks",
            textDecoration = TextDecoration.Underline,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.White,
        )

        LazyVerticalGrid(
            modifier = Modifier
                .background(Color(252, 61, 61))
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 5.dp, vertical = 5.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            if(uiState.decks.isEmpty()) {
                item {
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
                        onClick = {}
                    )
                }
            }
        }

        Button(
            onClick = onCreateDeckClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(117,255,114),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 20.dp),
            border = BorderStroke(1.dp, Color.Black),
            content = { Text("Create Profile") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksCard(
    modifier: Modifier = Modifier,
    deck: Deck,
    onClick: () -> Unit
) {
    Card (
        onClick = onClick,
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
            val image = when(deck.color) {
                1 -> painterResource(R.drawable.red)
                2 -> painterResource(R.drawable.blue)
                3 -> painterResource(R.drawable.yellow)
                4 -> painterResource(R.drawable.purple)
                else -> {
                    painterResource(R.drawable.ic_broken_image)
                }
            }

            Image(painter = image, contentDescription = null, modifier = Modifier)

            Text(
                text = deck.name
                , fontSize = 12.sp
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DecksListBodyPreview() {
    PokemonTCGManagerTheme {
        DecksListBody(
            contentPadding = PaddingValues(0.dp),
            onCreateDeckClick = { /*TODO*/ },
            uiState = UserWithDecksUiState(
                User(1, "Test", 1),
                listOf(
                    Deck(1, "Fun", "deck non competitif, pour les copaing", listOf(), 1,1),
                    Deck(2, "Try hard", "volonté de nuire", listOf(), 1, 1),
                    Deck(3, "Fun", "deck non competitif, pour les copaing", listOf(), 1,1),
                    Deck(4, "Try hard", "volonté de nuire", listOf(), 1, 1)
                )
            )
        )
    }
}