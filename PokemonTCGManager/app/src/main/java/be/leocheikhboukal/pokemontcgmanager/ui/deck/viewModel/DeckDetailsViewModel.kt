package be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCardDetails
import be.leocheikhboukal.pokemontcgmanager.ui.deck.screen.DeckDetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeckDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    val deckId: Int = checkNotNull(savedStateHandle[DeckDetailsDestination.DECK_ID_ARG])

    var deckUiState by mutableStateOf(DeckUiState())

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    init {
        viewModelScope.launch {
            deckUiState = decksRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
            val newListCard: MutableList<Card> = deckUiState.cards.toMutableList()
            deckUiState.deckDetails.cardList.forEach {
                newListCard.add(getCardDetails(tcgDexApi, it))
            }
            deckUiState.cards = newListCard.toList()
            _cards.value = newListCard
        }
    }

    suspend fun deleteDeck() {
        decksRepository.deleteDeck(deckUiState.deckDetails.toDeck())
    }

}