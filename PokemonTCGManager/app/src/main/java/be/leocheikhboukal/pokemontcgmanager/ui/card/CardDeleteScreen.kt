package be.leocheikhboukal.pokemontcgmanager.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckUiState
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


object CardDeleteDestination : NavigationDestination {
    override val route = "card_delete"
    const val USER_ID_ARG = "userId"
    const val DECK_ID_ARG = "cardId"
    val routeWithArgs = "${route}/{$USER_ID_ARG}/{$DECK_ID_ARG}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDeleteScreen(
    navigateToDeckDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navigateToCardSearch: (Int) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    onNavigateUp: () -> Unit,
    viewModel: CardDeleteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

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
                userId = viewModel.userId,
                navigateToCardSearch = navigateToCardSearch,
                navigateToDecksList = navigateToDeck,
                navigateToProfile = navigateToUser
            )

            CardDeleteBody(
                uiState = viewModel.deckUiState,
                onClickCard = { cardId: String ->
                    coroutineScope.launch {
                        viewModel.deleteCard(cardId)
                        navigateToDeckDetails(viewModel.deckId)
                    }
                }
            )
        }

    }
}


@Composable
fun CardDeleteBody(
    uiState: DeckUiState,
    onClickCard: (String) -> Unit,
){

    Column(
        modifier = Modifier
            .background(Color(252, 61, 61)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(
                text = "Select a card to remove from your deck",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color(252, 61, 61))
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .fillMaxSize(),
            ) {

                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Top
                ) {
                    val cardsWithImages = uiState.cards.filter { it.image != null }

                    items(cardsWithImages) { card ->
                        CardItem(
                            card = card,
                            onClickCard = onClickCard
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CardItem(card: Card, onClickCard: (String) -> Unit) {
    Card (
        onClick = {
            onClickCard(card.id)
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(10.dp)

    ) {

        AsyncImage (
            model = "${card.image}/high.png",
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null
        )

    }
}

@Preview(showBackground = true)
@Composable
fun CardDeleteBodyPreview() {

    PokemonTCGManagerTheme {
        CardDeleteBody(
            uiState = DeckUiState(),
            onClickCard = {} ,
        )
    }
}
