package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import kotlinx.coroutines.launch

object DeckModifyDestination: NavigationDestination {
    override val route: String = "deck_modify"
    const val DECK_ID_ARG = "deckId"
    val routeWithArgs = "$route/{$DECK_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckModifyScreen(
    navigateToDeckDetails: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    navigateToCardSearch: (Int) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: DeckModifyViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
            DeckModifyBody(
                modifier = Modifier,
                deckUiState = viewModel.deckUiState,
                onDeckValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateDeck()
                        navigateToDeckDetails(viewModel.deckUiState.deckDetails.id)
                    }
                }
            )
        }

    }
}

@Composable
fun DeckModifyBody(
    modifier: Modifier = Modifier,
    deckUiState: DeckUiState,
    onDeckValueChange: (DeckDetails) -> Unit,
    onSaveClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .padding(68.dp),
    ) {
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
                text = "Modify Deck",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            MyDropdownMenu(deckUiState = deckUiState, onValueChange = onDeckValueChange)

            OutlinedTextField(
                value = deckUiState.deckDetails.name,
                onValueChange = { onDeckValueChange(deckUiState.deckDetails.copy(name = it)) },
                label = { Text("Name*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = enabled,
                singleLine = true,
            )

            OutlinedTextField(
                value = deckUiState.deckDetails.description,
                onValueChange = { onDeckValueChange(deckUiState.deckDetails.copy(description = it)) },
                label = { Text(text ="Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = enabled,
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(117,255,114),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp, vertical = 20.dp),
                enabled = deckUiState.isEntryValid,
                border = BorderStroke(1.dp, Color.Black),
                content = { Text("Save Changes") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckModifyBodyPreview() {
    PokemonTCGManagerTheme {
        DeckAddBody(
            onSaveClick = {},
            deckUiState = DeckUiState(),
            onDeckValueChange = {}
        )
    }
}