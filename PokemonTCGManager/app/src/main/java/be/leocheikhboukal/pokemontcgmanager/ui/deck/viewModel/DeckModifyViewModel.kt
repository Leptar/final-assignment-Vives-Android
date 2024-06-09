package be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import be.leocheikhboukal.pokemontcgmanager.ui.deck.screen.DeckModifyDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeckModifyViewModel(
    savedStateHandle: SavedStateHandle,
    private val deckRepository: DecksRepository
) : ViewModel() {

    var deckUiState by mutableStateOf(DeckUiState())
        private set

    private val deckId: Int = checkNotNull(savedStateHandle[DeckModifyDestination.DECK_ID_ARG])

    init {
        viewModelScope.launch {
            deckUiState = deckRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
        }
    }

    /**
     * Updates the [DeckUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(deckDetails: DeckDetails) {
        deckUiState =
            DeckUiState(deckDetails = deckDetails, isEntryValid = validateInput(deckDetails))
    }

    /**
     * Update the deck in the [DecksRepository]'s data source
     */
    suspend fun updateDeck() {
        if (validateInput()) {
            deckRepository.updateDeck(deckUiState.deckDetails.toDeck())
        }
    }

    private fun validateInput(uiState: DeckDetails = deckUiState.deckDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && name.length <= 12 && category > 0 && category < 5
        }
    }
}
