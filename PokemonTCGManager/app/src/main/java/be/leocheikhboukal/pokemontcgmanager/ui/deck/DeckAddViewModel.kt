package be.leocheikhboukal.pokemontcgmanager.ui.deck

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository

class DeckAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[DecksListDestination.USER_ID_ARG])

    var deckUiState by mutableStateOf(DeckUiState(deckDetails = DeckDetails(userId = userId)))
        private set

    fun updateUiState(deckDetails: DeckDetails) {
        deckUiState =
            DeckUiState(deckDetails = deckDetails, isEntryValid = validateEntry(deckDetails))
    }

    suspend fun addDeck() {
        if (validateEntry()) {
            decksRepository.insertDeck(deckUiState.deckDetails.toDeck())
        }
    }

    private fun validateEntry(uiState: DeckDetails = deckUiState.deckDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && category < 5 && category > 0 && description.length < 128
        }
    }
}

data class DeckUiState(
    val deckDetails: DeckDetails = DeckDetails(),
    val isEntryValid: Boolean = false,
    var cards: List<Card> = emptyList()
)

data class DeckDetails (
    val id: Int = 0,
    val name: String = "test",
    val description: String = "test",
    val cardList: List<String> = emptyList(),
    var category: Int = 1,
    val userId: Int = 1
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

fun Deck.toDeckUiState(isEntryValid: Boolean): DeckUiState =
    DeckUiState(
        deckDetails = this.toDeckDetails(),
        isEntryValid = isEntryValid
    )

fun Deck.toDeckDetails(): DeckDetails =
    DeckDetails(
        id = id,
        name = name,
        description = description,
        cardList = cardList,
        category = category,
        userId = userId
    )