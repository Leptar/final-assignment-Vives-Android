package be.leocheikhboukal.pokemontcgmanager.ui.card.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardUiState
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCardByName
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCardDetails
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCards
import be.leocheikhboukal.pokemontcgmanager.ui.card.screen.CardAddDestination
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.DeckUiState
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.toDeck
import be.leocheikhboukal.pokemontcgmanager.ui.deck.viewModel.toDeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    // Arguments
    val userId: Int = checkNotNull(savedStateHandle[CardAddDestination.USER_ID_ARG])
    val deckId: Int = checkNotNull(savedStateHandle[CardAddDestination.DECK_ID_ARG])

    // State
    private var deckUiState by mutableStateOf(DeckUiState())
    private var _cardsUiState = MutableStateFlow(CardUiState())
    var cardUiState: MutableStateFlow<CardUiState> = _cardsUiState
        private set

    // API
    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    // initialize the ViewModel with the cards in the API and the deck
    init {
        getCards(
            viewModelScope = viewModelScope,
            tcgDexApi = tcgDexApi,
            cardsUiState = _cardsUiState
        )

        viewModelScope.launch {
            deckUiState = decksRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
        }
    }

    suspend fun updateDeck(cardId: String) {
        // Update the deck with the new card
        val newListCard: MutableList<String> = deckUiState.deckDetails.cardList.toMutableList()
        newListCard.add(cardId)
        decksRepository.updateDeck(deckUiState.deckDetails.copy(cardList = newListCard.toList()).toDeck())

        // update the cards list in deckUiState
        val newListOfCards: MutableList<Card> = deckUiState.cards.toMutableList()
        newListOfCards.add(getCardDetails(tcgDexApi,cardId))
        deckUiState = DeckUiState(
            deckDetails = deckUiState.deckDetails.copy(cardList = newListCard.toList()),
            isEntryValid = true,
            cards = newListOfCards.toList()
        )

    }

    fun updateNameSearch(name: String) {
        be.leocheikhboukal.pokemontcgmanager.ui.card.updateNameSearch(cardUiState, name)
    }

    fun updateCards() {
        getCardByName(
            viewModelScope = viewModelScope,
            tcgDexApi = tcgDexApi,
            name = cardUiState.value.nameSearch,
            cardsUiState = _cardsUiState
        )
    }
}