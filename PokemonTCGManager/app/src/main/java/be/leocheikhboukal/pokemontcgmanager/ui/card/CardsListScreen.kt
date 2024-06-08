package be.leocheikhboukal.pokemontcgmanager.ui.card

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.data.CardBrief
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import coil.compose.AsyncImage

object CardsListDestination : NavigationDestination {
    override val route = "cards_list"
    const val USER_ID_ARG = "userId"
    val routeWithArgs = "${CardsListDestination.route}/{$USER_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsListScreen(
    modifier: Modifier = Modifier,
    navigateToCardSearch: (Int) -> Unit,
    navigateToCardDetail: (String) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    onNavigateUp: () -> Unit,
    viewModel: CardsListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


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

            CardsListBody(
                listCards = viewModel.cardsLiveData.value,
                navigateToCardDetail = navigateToCardDetail
            )
        }

    }
}


@Composable
fun CardsListBody(
    listCards: List<CardBrief>?,
    navigateToCardDetail: (String) -> Unit
){
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
    ){
        Row(
            modifier = Modifier.weight(0.09f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = "",
                onValueChange = { },
                label = { Text("search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                singleLine = true,
            )

            Button(
                onClick = { /*TODO*/ },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "",
                    )
                },
                modifier = Modifier
                    .padding(5.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f)
                .padding(2.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Top
        ) {
            val cardsWithImages = listCards?.filter { it.image != null }

            if (cardsWithImages != null) {
                items(cardsWithImages) { card ->
                    CardItem(
                        card = card,
                        onClickCard = navigateToCardDetail
                    )
                }
            } else {
                item{
                    Text(
                        text = "No cards found",
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(
    card: CardBrief,
    onClickCard: (String) -> Unit
) {
    Card (
        onClick = { onClickCard(card.id) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(10.dp)

    ) {

        AsyncImage (
            model = "${card.image}/high.png",
            placeholder = painterResource(id = R.drawable.yellow),
            error = painterResource(id = R.drawable.red),
            contentDescription = null
        )

    }
}

@Preview(showBackground = true)
@Composable
fun CardsListBodyPreview() {
    PokemonTCGManagerTheme {
        CardsListBody(
            listCards = null,
            navigateToCardDetail = {}
        )
    }
}

