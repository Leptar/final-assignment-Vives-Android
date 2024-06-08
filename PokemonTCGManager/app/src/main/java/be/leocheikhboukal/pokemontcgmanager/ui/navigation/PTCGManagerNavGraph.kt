package be.leocheikhboukal.pokemontcgmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardAddScreen
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardDetailsDestination
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardDetailsScreen
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardsListDestination
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardsListScreen
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckAddScreen
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckDetailsDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckDetailsScreen
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckModifyDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckModifyScreen
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DecksListDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DecksListScreen
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeDestination
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeScreen
import be.leocheikhboukal.pokemontcgmanager.ui.user.UserAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.user.UserAddScreen
import be.leocheikhboukal.pokemontcgmanager.ui.user.UserDetailsDestination
import be.leocheikhboukal.pokemontcgmanager.ui.user.UserDetailsScreen

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
                navigateProfileAdd = { navController.navigate(UserAddDestination.route) },
                navigateToDecksUser = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                }
            )
        }

        composable(route = UserAddDestination.route) {
           UserAddScreen(
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
                navigateToDeck =  {
                    navController.navigate("${DeckDetailsDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                                       },
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
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                }

            )
        }
        
        composable(
            route = UserDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(UserDetailsDestination.USER_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            UserDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                } ,
                navigateToLogin = {
                    navController.navigate(HomeDestination.route)
                })
        }

        composable(
            route = DeckDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DeckDetailsDestination.DECK_ID_ARG) {
                type = NavType.IntType
            })
        ){
            DeckDetailsScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToCardSearch = { navController.navigate("${CardsListDestination.route}/${it}") },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateBack = { navController.popBackStack() },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                onAddCardToDeck = { deckId: Int, userId: Int, ->
                    navController.navigate("${CardAddDestination.route}/${userId}/${deckId}")
                },
                onRemoveCardFromDeck = { /*TODO*/ },
                onModifyDeck = {
                    navController.navigate("${DeckModifyDestination.route}/${it}")
                },
                navigateToCardDetail = { cardId: String, userId: Int, ->
                    navController.navigate("${CardDetailsDestination.route}/${userId}/${cardId}")
                }
            )
        }

        composable(
            route = DeckModifyDestination.routeWithArgs,
            arguments = listOf(navArgument(DeckModifyDestination.DECK_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            DeckModifyScreen(
                navigateToDeckDetails = {
                    navController.navigate("${DeckDetailsDestination.route}/${it}")
                },
                onNavigateUp = { navController.navigateUp() },
                navigateToCardSearch = { navController.navigate("${CardsListDestination.route}/${it}") },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                }

            )
        }

        composable(
            route = CardsListDestination.routeWithArgs,
            arguments = listOf(navArgument(CardsListDestination.USER_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            CardsListScreen(
                modifier = Modifier,
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                canNavigateBack = true,
                onNavigateUp = { navController.navigateUp() },
                navigateToCardDetail = { cardId: String, userId: Int, ->
                    navController.navigate("${CardDetailsDestination.route}/${userId}/${cardId}")
                }
            )
        }

        composable(
            route = CardDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CardDetailsDestination.USER_ID_ARG) {
                    type = NavType.IntType
                },
                navArgument(CardDetailsDestination.CARD_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            CardDetailsScreen(
                canNavigateBack = true,
                onNavigateUp = { navController.navigateUp() },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                }
            )
        }

        composable(
            route = CardAddDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CardAddDestination.DECK_ID_ARG) {
                    type = NavType.IntType
                },
                navArgument(CardAddDestination.USER_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) {
            CardAddScreen(
                canNavigateBack = true,
                onNavigateUp = { navController.navigateUp() },
                navigateToDeckDetails = {
                    navController.navigate("${DeckDetailsDestination.route}/${it}")
                },
                navigateToDeck = {
                    navController.navigate("${DecksListDestination.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserDetailsDestination.route}/${it}")
                },
                navigateToCardSearch = {
                    navController.navigate("${CardsListDestination.route}/${it}")
                }
            )
        }

    }
}