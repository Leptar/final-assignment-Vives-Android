package be.leocheikhboukal.pokemontcgmanager.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import kotlinx.coroutines.launch

object ProfileAddDestination : NavigationDestination {
    override val route: String = "profile/add"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: ProfileAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        ProfileAddBody(
            modifier = Modifier,
            profileUiState = viewModel.profileAddUiState,
            contentPadding = innerPadding,
            onProfileValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveUser()
                    navigateBack()
                }
            }
        )
    }
}

@Composable
fun ProfileAddBody(
    modifier: Modifier = Modifier,
    profileUiState: ProfileAddUiState,
    onProfileValueChange: (ProfileDetails) -> Unit,
    onSaveClick: () -> Unit,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 68.dp, vertical = 150.dp),
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
                text = "Add Profile",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            MyDropdownMenu(profileUiState = profileUiState)

            OutlinedTextField(
                value = profileUiState.profileDetails.name,
                onValueChange = { onProfileValueChange(profileUiState.profileDetails.copy(name = it)) },
                label = { Text("Name*") },
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(16.dp),
                enabled = enabled,
                singleLine = true,
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
                enabled = profileUiState.isEntryValid,
                border = BorderStroke(1.dp, Color.Black),
                content = { Text("Create Profile") }
            )
        }
    }
}


@Composable
fun MyDropdownMenu(
    profileUiState: ProfileAddUiState,
    onValueChange: (ProfileDetails) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val text = when(profileUiState.profileDetails.color) {
        1 -> "Red"
        2 -> "Blue"
        3 -> "Yellow"
        4 -> "Purple"
        else -> {
            ""
        }
    }

    Column {
        TextField(
            value = text ,
            onValueChange = { onValueChange(profileUiState.profileDetails.copy(color = it.toInt())) },
            label = { Text("Select a color*") },
            readOnly = false,
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
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Red") },
                onClick = {
                    profileUiState.profileDetails.color = 1
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Blue") },
                onClick = {
                    profileUiState.profileDetails.color = 2
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Yellow") },
                onClick = {
                    profileUiState.profileDetails.color = 3
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Purple") },
                onClick = {
                    profileUiState.profileDetails.color = 4
                    expanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    PokemonTCGManagerTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            topBar = {
                PTCGManagerTitleAppBar(
                    scrollBehavior = scrollBehavior,
                    canNavigateBack = true,
                    navigateUp = {}
                )
            },
            content = { innerPadding ->
                ProfileAddBody(
                    contentPadding = innerPadding,
                    onSaveClick = {},
                    profileUiState = ProfileAddUiState(),
                    onProfileValueChange = {}
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
fun ProfileAddBodyPreview() {
    PokemonTCGManagerTheme {
        ProfileAddBody(
            onSaveClick = {},
            profileUiState = ProfileAddUiState(),
            onProfileValueChange = {}
        )
    }
}