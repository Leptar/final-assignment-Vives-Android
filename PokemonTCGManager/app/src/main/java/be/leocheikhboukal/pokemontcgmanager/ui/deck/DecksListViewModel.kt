package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.data.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DecksListViewModel(
    savedStateHandle: SavedStateHandle,
    private val usersRepository: UserRepository
) : ViewModel() {
    private val userId: Int = checkNotNull(savedStateHandle[DecksListDestination.USER_ID_ARG])

    val stateFlowUiState: StateFlow<UserWithDecksUiState> =
        usersRepository.getUserAndDecksStream(userId)
            .filterNotNull()
            .map{
                UserWithDecksUiState(it.user, it.decks)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserWithDecksUiState()
            )

}

data class UserWithDecksUiState(
    val user: User = User(1, "", 0),
    val decks: List<Deck> = emptyList()
)