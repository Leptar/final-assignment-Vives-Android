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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.R
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import kotlinx.coroutines.launch

object DeckAddDestination : NavigationDestination {
    override val route: String = "deck_add"
    const val USER_ID_ARG = "userId"
    val routeWithArgs = "$route/{$USER_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToCardSearch: () -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: DeckAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
            DeckAddBody(
                modifier = Modifier,
                deckUiState = viewModel.deckUiState,
                onDeckValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.addDeck()
                        navigateBack()
                    }
                }
            )
        }

    }
}

@Composable
fun DeckAddBody(
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
                text = "Add new deck",
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
                content = { Text("Create") }
            )
        }
    }
}


@Composable
fun MyDropdownMenu(
    deckUiState: DeckUiState,
    onValueChange: (DeckDetails) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val text = when(deckUiState.deckDetails.category) {
        1 -> stringResource(R.string.category_usable)
        2 -> stringResource(R.string.category_need_to_test)
        3 -> stringResource(R.string.category_need_to_change)
        4 -> stringResource(R.string.category_not_finished)
        else -> {
            ""
        }
    }

    Column {
        TextField(
            value = text ,
            onValueChange = { onValueChange(deckUiState.deckDetails.copy(category = it.toInt())) },
            label = { Text("Select a category*") },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown arrow",
                    )
                }
            },
            keyboardActions = KeyboardActions(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.category_usable)) },
                onClick = {
                    deckUiState.deckDetails.category = 1
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.category_need_to_test)) },
                onClick = {
                    deckUiState.deckDetails.category = 2
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.category_need_to_change)) },
                onClick = {
                    deckUiState.deckDetails.category = 3
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.category_not_finished)) },
                onClick = {
                    deckUiState.deckDetails.category = 4
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckAddBodyPreview() {
    PokemonTCGManagerTheme {
        DeckAddBody(
            onSaveClick = {},
            deckUiState = DeckUiState(),
            onDeckValueChange = {}
        )
    }
}