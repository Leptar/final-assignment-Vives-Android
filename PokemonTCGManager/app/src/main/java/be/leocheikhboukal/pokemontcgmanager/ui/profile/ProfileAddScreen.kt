package be.leocheikhboukal.pokemontcgmanager.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.leocheikhboukal.pokemontcgmanager.PTCGManagerTitleAppBar
import be.leocheikhboukal.pokemontcgmanager.ui.AppViewModelProvider
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme

object ProfileAddDestination : NavigationDestination {
    override val route: String = "profile/add"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAddScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(252, 61, 61)),
        topBar = {
            PTCGManagerTitleAppBar(
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color(252,61,61),
    ) { innerPadding ->
        ProfileAddBody(
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ProfileAddBody(
    modifier: Modifier = Modifier,
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
                text = "Select your Profile",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAddBodyPreview() {
    PokemonTCGManagerTheme {
        ProfileAddBody()
    }
}