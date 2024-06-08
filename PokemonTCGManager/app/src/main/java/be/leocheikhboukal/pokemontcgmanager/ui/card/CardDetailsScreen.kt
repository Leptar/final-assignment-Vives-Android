package be.leocheikhboukal.pokemontcgmanager.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import be.leocheikhboukal.pokemontcgmanager.data.CardCount
import be.leocheikhboukal.pokemontcgmanager.data.SetBrief
import be.leocheikhboukal.pokemontcgmanager.data.Variants
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import coil.compose.AsyncImage

object CardDetailsDestination : NavigationDestination {
    override val route = "card_details"
    const val USER_ID_ARG = "userId"
    const val CARD_ID_ARG = "cardId"
    val routeWithArgs = "${route}/{$USER_ID_ARG}/{$CARD_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit,
    navigateToCardSearch: (Int) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
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

            CardDetailsBody(
                card = viewModel.cardsLiveData.value
            )
        }

    }

}

@Composable
fun CardDetailsBody(
    card: Card?,
    modifier: Modifier = Modifier
) {
    var description: String = when(card?.category) {
        "Energy" -> card.effect.toString()
        "Pokemon" -> card.description.toString()
        "Trainer"-> card.effect.toString()
        else -> {"No description"}
    }

    description = if (description == "null" || description == "")
        "No description" else description

    Column(
        modifier = modifier
            .background(Color(252, 61, 61))
            .padding(horizontal = 15.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = modifier
                .background(Color(252, 61, 61))
                .weight(1f)
        ){
            AsyncImage (
                model = "${card?.image}/high.png",
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Row(
            modifier = modifier
                .background(Color(252, 61, 61))
                .padding(top = 5.dp)
        ){
            Column(
                modifier = Modifier
                    .background(Color(252, 61, 61))
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .fillMaxWidth(),
            ) {
                Row{
                    Text(
                        text = "Information :",
                        fontSize = 20.sp,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Name :",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = "Category :",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = "Rarity :",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = "Set :",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = "Description :",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )

                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = card?.name ?: "",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = card?.category ?: "",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = card?.rarity ?: "",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = card?.set?.name ?: "",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp)
                        )

                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = description,
                        color = Color.Black,
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardDetailsBodyPreview() {
    PokemonTCGManagerTheme {
        val exampleCard = Card(
            id = "xy7-54",
            localId = 54,
            name = "Pikachu",
            image = "https://images.pokemontcg.io/xy7/54.png",
            category = "Pokemon",
            illustrator = "5ban Graphics",
            rarity = "Rare Holo",
            variants = Variants(
                normal = true,
                reverse = true,
                holo = true,
                firstEdition = false
            ),
            set = SetBrief(
                id = "xy7",
                name = "Ancient Origins",
                logo = "https://images.pokemontcg.io/xy7/logo.png",
                symbol = "https://images.pokemontcg.io/xy7/symbol.png",
                cardCount = CardCount(
                    total = 100,
                    official = 98
                )
            ),
            dexId = listOf(25),
            hp = 60,
            types = listOf("Lightning"),
            evolveFrom = "Pichu",
            description = "Pikachu that can generate powerful electricity have cheek sacs that are extra soft and super stretchy.",
            level = "12",
            stage = "Basic",
            suffix = null,
            item = null,
            effect = null,
            trainerType = null,
            energyType = null
        )
        CardDetailsBody(
            card = exampleCard
        )
    }
}