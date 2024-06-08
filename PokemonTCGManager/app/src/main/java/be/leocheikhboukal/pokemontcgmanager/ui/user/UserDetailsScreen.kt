package be.leocheikhboukal.pokemontcgmanager.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerSubAppBar
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme
import kotlinx.coroutines.launch

object UserDetailsDestination : NavigationDestination {
    override val route: String = "user_details"
    const val USER_ID_ARG = "userId"
    val routeWithArgs = "$route/{$USER_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToCardSearch: (Int) -> Unit,
    navigateToDeck: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    navigateToLogin: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: UserDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                userId = viewModel.userUiState.userDetails.id,
                navigateToCardSearch = navigateToCardSearch,
                navigateToDecksList = navigateToDeck,
                navigateToProfile = navigateToUser
            )
            UserDetailsBody(
                modifier = Modifier,
                userUiState = viewModel.userUiState,
                onUserValueChange = viewModel::updateUiState,
                navigateToLogin = navigateToLogin,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateUser()
                        navigateBack()
                    }
                }
            )
        }
    }
}

@Composable
fun UserDetailsBody(
    modifier: Modifier = Modifier,
    userUiState: UserUiState,
    onUserValueChange: (UserDetails) -> Unit,
    onSaveClick: () -> Unit,
    enabled: Boolean = true,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(68.dp)
            .background(Color(252, 61, 61)),
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
                text = "Profile",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            MyDropdownMenu(userUiState = userUiState)

            OutlinedTextField(
                value = userUiState.userDetails.name,
                onValueChange = { onUserValueChange(userUiState.userDetails.copy(name = it)) },
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
                enabled = userUiState.isEntryValid,
                border = BorderStroke(1.dp, Color.Black),
                content = { Text("Modify Profile") }
            )
        }

        Button(
            onClick = navigateToLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 15.dp),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            content = { Text("Switch Profile") }
        )

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(232,236,15),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            border = BorderStroke(3.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            content = {
                Text(
                    text = "Delete Profile",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsBodyPreview() {
    PokemonTCGManagerTheme {
        UserDetailsBody(
            userUiState = UserUiState(),
            onUserValueChange = {},
            onSaveClick = {},
            navigateToLogin = { }
        )
    }
}