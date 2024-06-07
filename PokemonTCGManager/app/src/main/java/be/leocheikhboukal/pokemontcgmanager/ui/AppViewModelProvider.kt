package be.leocheikhboukal.pokemontcgmanager.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.leocheikhboukal.pokemontcgmanager.PokemonTCGManagerApplication
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardsListViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckAddViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckDetailsViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckModifyViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DecksListViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.home.HomeViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.user.ProfileAddViewModel
import be.leocheikhboukal.pokemontcgmanager.ui.user.UserDetailsViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(PokemonTCGManagerApplication().container.usersRepository)
        }

        // Initializer for ProfileAddViewModel
        initializer {
            ProfileAddViewModel(PokemonTCGManagerApplication().container.usersRepository)
        }

        // Initializer for DecksListViewModel
        initializer {
            DecksListViewModel(
                this.createSavedStateHandle(),
                PokemonTCGManagerApplication().container.usersRepository
            )
        }

        // Initializer for DeckAddViewModel
        initializer {
            DeckAddViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                decksRepository = PokemonTCGManagerApplication().container.decksRepository
            )
        }

        // Initializer for UserDetailsViewModel
        initializer {
            UserDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                userRepository = PokemonTCGManagerApplication().container.usersRepository
            )
        }

        // Initializer for DeckDetailsViewModel
        initializer {
            DeckDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                decksRepository = PokemonTCGManagerApplication().container.decksRepository
            )
        }

        // Initializer for DeckModifyViewModel
        initializer {
            DeckModifyViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                deckRepository = PokemonTCGManagerApplication().container.decksRepository
            )
        }

        // Initializer for CardsListViewModel
        initializer {
            CardsListViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                usersRepository = PokemonTCGManagerApplication().container.usersRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PokemonTCGManagerApplication].
 */
fun CreationExtras.PokemonTCGManagerApplication(): PokemonTCGManagerApplication =
    (this[APPLICATION_KEY] as PokemonTCGManagerApplication)