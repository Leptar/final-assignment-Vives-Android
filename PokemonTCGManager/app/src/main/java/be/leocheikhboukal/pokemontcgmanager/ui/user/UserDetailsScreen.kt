package be.leocheikhboukal.pokemontcgmanager.ui.user

import be.leocheikhboukal.pokemontcgmanager.ui.navigation.NavigationDestination

object UserDetailsDestination : NavigationDestination {
    override val route: String = "user_details"
    const val USER_ID_ARG = "userId"
    val routeWithArgs = "$route/{$USER_ID_ARG}"
}

fun userDetailsScreen(

) {

}