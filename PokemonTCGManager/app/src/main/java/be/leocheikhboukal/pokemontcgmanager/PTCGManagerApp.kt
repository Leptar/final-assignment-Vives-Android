package be.leocheikhboukal.pokemontcgmanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
@Composable
fun PTCGManagerTitleAppBar(
    modifier: Modifier = Modifier,
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(32,32,32))
            .padding(5.dp)
    ){
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
            lineHeight = 30.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TitleBarPreview() {
    PokemonTCGManagerTheme {
        PTCGManagerTitleAppBar()
    }
}
