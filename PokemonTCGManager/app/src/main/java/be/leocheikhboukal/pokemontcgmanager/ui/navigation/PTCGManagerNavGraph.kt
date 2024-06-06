package be.leocheikhboukal.pokemontcgmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckAddScreen
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DecksListDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DecksListScreen
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeDestination
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeScreen
import be.leocheikhboukal.pokemontcgmanager.ui.user.ProfileAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.user.ProfileAddScreen

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
            HomeScreen(
                navigateProfileAdd = { navController.navigate(ProfileAddDestination.route) },
                navigateToDecksUser = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                }
            )
        }

        composable(route = ProfileAddDestination.route) {
           ProfileAddScreen(
               navigateBack = { navController.popBackStack() },
               onNavigateUp = { navController.navigateUp() }
           )
        }

        composable(
            route = DecksListDestination.routeWithArgs,
            arguments = listOf(navArgument(DecksListDestination.USER_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            DecksListScreen(
                navigateToDeck =  {},
                navigateToUser = {},
                navigateToCardSearch = {},
                navigateToDeckCreate = {
                    navController.navigate("${DeckAddDestination.route}/${it}")
                }
            )
        }

        composable(
            route = DeckAddDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DeckAddDestination.USER_ID_ARG) {
                    type = NavType.IntType
                },
            )
        ){
            DeckAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {},
                navigateToCardSearch = {}

            )
        }
    }
}