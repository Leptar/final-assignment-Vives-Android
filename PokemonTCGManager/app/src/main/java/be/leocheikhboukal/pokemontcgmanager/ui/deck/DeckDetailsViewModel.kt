package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeckDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {


    private val deckId: Int = checkNotNull(savedStateHandle[DeckDetailsDestination.DECK_ID_ARG])

    var deckUiState by mutableStateOf(DeckUiState())
        private set

    init {
        viewModelScope.launch {
            deckUiState = decksRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
        }
    }

    suspend fun deleteDeck() {
        decksRepository.deleteDeck(deckUiState.deckDetails.toDeck())
    }

}