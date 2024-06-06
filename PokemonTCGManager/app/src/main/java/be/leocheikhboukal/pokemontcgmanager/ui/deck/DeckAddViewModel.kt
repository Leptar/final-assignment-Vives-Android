package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository

class DeckAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[DecksListDestination.USER_ID_ARG])

    var deckAddUiState by mutableStateOf(DeckAddUiState(deckDetails = DeckDetails(userId = userId)))
        private set

    fun updateUiState(deckDetails: DeckDetails) {
        deckAddUiState =
            DeckAddUiState(deckDetails = deckDetails, isEntryValid = validateEntry(deckDetails))
    }

    suspend fun addDeck() {
        if (validateEntry()) {
            decksRepository.insertDeck(deckAddUiState.deckDetails.toDeck())
        }
    }

    private fun validateEntry(uiState: DeckDetails = deckAddUiState.deckDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && category < 5 && category > 0 && description.length < 128
        }
    }
}

data class DeckAddUiState(
    val deckDetails: DeckDetails = DeckDetails(),
    val isEntryValid: Boolean = false
)

data class DeckDetails (
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cardList: List<String> = emptyList(),
    var category: Int = 0,
    val userId: Int = 0
)

fun DeckDetails.toDeck(): Deck =
    Deck(
        id = id,
        name = name,
        description = description,
        cardList = cardList,
        category = category,
        userId = userId
    )
