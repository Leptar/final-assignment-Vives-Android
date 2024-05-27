package be.leocheikhboukal.pokemontcgmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeDestination
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeScreen

/**
 * Navigation graph for the application
 */
@Composable
fun PTCGManagerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen()
        }
    }
}