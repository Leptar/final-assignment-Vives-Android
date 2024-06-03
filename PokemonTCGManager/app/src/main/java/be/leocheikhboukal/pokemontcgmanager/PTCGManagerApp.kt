package be.leocheikhboukal.pokemontcgmanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import be.leocheikhboukal.pokemontcgmanager.ui.navigation.PTCGManagerNavHost
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme

@Composable
fun PTCGManagerApp(navController: NavHostController = rememberNavController()) {
    PTCGManagerNavHost(navController = navController)
}

/**
 * App Bar to diplay name of the application and navigate part if choose
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PTCGManagerTitleAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {}
) {

    CenterAlignedTopAppBar(
        title = {
            Row{
                Image(
                    painter = painterResource(id = R.drawable.title_default_image),
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color(32,32,32), RoundedCornerShape(40.dp))
                        .size(80.dp)
                        .padding(5.dp)
                )
                Text(
                    text = "POKEMON \nTCG MANAGER",
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    fontSize = 32.sp,
                    lineHeight = 28.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }

        },
        modifier = modifier
            .background(Color(32,32,32))
            .padding(5.dp),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(32,32,32)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TitleBarPreview() {
    PokemonTCGManagerTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        PTCGManagerTitleAppBar(
            scrollBehavior = scrollBehavior
        )
    }
}
