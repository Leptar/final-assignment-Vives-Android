package be.leocheikhboukal.pokemontcgmanager.ui.deck.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.DeckDetails
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.DeckDetailsViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.DeckUiState
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object DeckDetailsDestination: NavigationDestination {
    override val route = "deck_details"
    const val DECK_ID_ARG = "deckId"
    val routeWithArgs = "$route/{$DECK_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToCardSearch: (Int) -> Unit,
    navigateToCardDetail: (String, Int) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    onAddCardToDeck: (Int, Int) -> Unit,
    onRemoveCardFromDeck: (Int, Int) -> Unit,
    onModifyDeck: (Int) -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: DeckDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                userId = viewModel.deckUiState.deckDetails.userId,
                navigateToCardSearch = navigateToCardSearch,
                navigateToDecksList = navigateToDeck,
                navigateToProfile = navigateToUser
            )
            DeckDetailsBody(
                cards = viewModel.cards,
                deckUiState = viewModel.deckUiState,
                onAddCardToDeck = onAddCardToDeck,
                onRemoveCardFromDeck = onRemoveCardFromDeck,
                onModifyDeck = onModifyDeck,
                onRemoveDeck = {
                    coroutineScope.launch {
                        viewModel.deleteDeck()
                        navigateBack()
                    }
                },
                navigateToCardDetail = navigateToCardDetail
            )
        }

    }
}

@Composable
fun DeckDetailsBody(
    modifier: Modifier = Modifier,
    deckUiState: DeckUiState,
    onAddCardToDeck: (Int, Int) -> Unit,
    onRemoveCardFromDeck: (Int, Int) -> Unit,
    onModifyDeck: (Int) -> Unit,
    onRemoveDeck: () -> Unit,
    navigateToCardDetail: (String, Int) -> Unit,
    cards: StateFlow<List<Card>>,
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(Color(252, 61, 61))
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        
        FirstRow(deckUiState)

        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .weight(0.25f)
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ){
                Text(
                    text = "Description",
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(5.dp)
                )

                Box(
                    modifier = Modifier
                        .background(Color(252, 61, 61))
                        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .fillMaxSize(),
                   contentAlignment = Alignment.Center,
                ) {
                    val description =
                        deckUiState.deckDetails.description.ifEmpty {
                            stringResource(R.string.no_description)
                        }
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .weight(1f)
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ){
                Text(
                    text = "Cards",
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Column(
                    modifier = Modifier
                        .background(Color(252, 61, 61))
                        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        val cardWithImage = cards.value.filter { it.image != null }
                        items(cardWithImage) { card ->
                            CardItem(
                                image = card.image,
                                userId = deckUiState.deckDetails.userId,
                                cardId = card.id,
                                onClickCard = navigateToCardDetail
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.Black)
                    )
                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                    ){
                        Button(
                            onClick = {
                                onAddCardToDeck(
                                    deckUiState.deckDetails.id,
                                    deckUiState.deckDetails.userId
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(117,255,114),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .padding(bottom = 5.dp, end = 5.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            content = {
                                Text(
                                    text = "Add a Card",
                                    fontSize = 15.sp
                                )
                            }
                        )

                        Button(
                            onClick = {
                                onRemoveCardFromDeck(
                                    deckUiState.deckDetails.id,
                                    deckUiState.deckDetails.userId
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(232,236,15),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .padding(bottom = 5.dp, end = 5.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(3.dp, Color.Black),
                            content = {
                                Text(
                                    text = "Delete Cards",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        )
                    }

                }
            }
        }

        BottomRow(
            onRemoveDeckClick = { showDialog = true },
            deckUiState = deckUiState,
            onModifyDeck = onModifyDeck
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this deck?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onRemoveDeck() // Call the onRemoveDeck lambda
                            showDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(
    image: String?,
    cardId: String,
    userId: Int,
    onClickCard: (String, Int) -> Unit
) {
    Card (
        onClick = { onClickCard(cardId, userId) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(10.dp)

    ) {

        AsyncImage (
            model = "${image}/high.png",
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null
        )

    }
}

@Composable
private fun BottomRow(
    onRemoveDeckClick: () -> Unit,
    deckUiState: DeckUiState,
    onModifyDeck: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = {onModifyDeck(deckUiState.deckDetails.id)},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(117, 255, 114),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
                .weight(1f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Black),
            content = {
                Text(
                    text = "Modify Deck",
                    fontSize = 20.sp
                )
            }
        )

        Button(
            onClick = onRemoveDeckClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(232, 236, 15),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
                .weight(1f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(3.dp, Color.Black),
            content = {
                Text(
                    text = "Delete Deck",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        )
    }
}

@Composable
private fun FirstRow(deckUiState: DeckUiState) {
    Row(
        modifier = Modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = deckUiState.deckDetails.name,
            fontSize = 32.sp,
            textDecoration = TextDecoration.Underline,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        )

        val image = when (deckUiState.deckDetails.category) {
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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(Color.White)
                .border(1.dp, Color.Black)
                .size(150.dp)
        )
    }
}

@Preview
@Composable
fun DeckDetailsBodyPreview() {
    val cards: MutableStateFlow<List<Card>> = MutableStateFlow(emptyList())
    PokemonTCGManagerTheme {
        DeckDetailsBody(
            deckUiState = DeckUiState(
                deckDetails = DeckDetails(description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sit amet lectus tempor, tincidunt quam sed, sagittis non.")
            ),
            onAddCardToDeck = { _, _ -> },
            onRemoveCardFromDeck = { _, _ -> },
            onModifyDeck = { },
            onRemoveDeck = { },
            navigateToCardDetail = { _, _ -> },
            cards = cards
        )
    }
}