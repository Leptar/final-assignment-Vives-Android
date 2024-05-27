package be.leocheikhboukal.pokemontcgmanager.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.leocheikhboukal.pokemontcgmanager.PokemonTCGManagerApplication
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(PokemonTCGManagerApplication().container.usersRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PokemonTCGManagerApplication].
 */
fun CreationExtras.inventoryApplication(): PokemonTCGManagerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonTCGManagerApplication)