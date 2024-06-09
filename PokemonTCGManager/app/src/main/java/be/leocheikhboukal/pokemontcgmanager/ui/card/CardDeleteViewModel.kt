package be.leocheikhboukal.pokemontcgmanager.ui.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.CardCount
import be.leocheikhboukal.pokemontcgmanager.data.SetBrief
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.data.Variants
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import be.leocheikhboukal.pokemontcgmanager.ui.deck.DeckUiState
import be.leocheikhboukal.pokemontcgmanager.ui.deck.toDeck
import be.leocheikhboukal.pokemontcgmanager.ui.deck.toDeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardDeleteViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[CardAddDestination.USER_ID_ARG])
    val deckId: Int = checkNotNull(savedStateHandle[CardAddDestination.DECK_ID_ARG])

    var deckUiState by mutableStateOf(DeckUiState())

    private val _cards = MutableStateFlow(deckUiState.cards)
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

//    private var _cardsUiState = MutableStateFlow(CardUiState())
//    var cardUiState: MutableStateFlow<CardUiState> = _cardsUiState
//        private set

    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    init {
        viewModelScope.launch {
            deckUiState = decksRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
            val newListCard: MutableList<Card> = deckUiState.cards.toMutableList()
            deckUiState.deckDetails.cardList.forEach {
                newListCard.add(getCardDetails(it))
            }
            deckUiState.cards = newListCard.toList()
            _cards.value = newListCard
        }

    }

    private suspend fun getCardDetails(id: String): Card {

        return try {
            tcgDexApi.getCardDetails(id)

        } catch (e: Exception) {
            // Handle network error
            Card(
                id = "error-000",
                localId = 0,
                name = "Error Card",
                image = null, // Or a placeholder error image URL
                category = "Unknown",
                illustrator = null,
                rarity = null,
                variants = Variants(
                    normal = false,
                    reverse = false,
                    holo = false,
                    firstEdition = false
                ),
                set = SetBrief(
                    id = "error-set",
                    name = "Error Set",
                    logo = null,
                    symbol = null,
                    cardCount = CardCount(
                        total = 0,
                        official = 0
                    )
                ),
                dexId = null,
                hp = null,
                types = null,
                evolveFrom = null,
                description = "An error occurred while loading card details.",
                level = null,
                stage = null,
                suffix = null,
                item = null,
                effect = null,
                trainerType = null,
                energyType = null
            )
        }
    }

    suspend fun deleteCard(idCard: String) {
        val newListCard: MutableList<String> = deckUiState.deckDetails.cardList.toMutableList()
        newListCard.remove(idCard)
        deckUiState.deckDetails.cardList = newListCard.toList()
        decksRepository.updateDeck(deckUiState.deckDetails.toDeck())
    }
}