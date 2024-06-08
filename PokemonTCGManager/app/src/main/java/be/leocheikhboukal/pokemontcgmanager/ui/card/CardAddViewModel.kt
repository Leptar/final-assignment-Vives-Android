package be.leocheikhboukal.pokemontcgmanager.ui.card

import android.util.Log
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val decksRepository: DecksRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[CardAddDestination.USER_ID_ARG])
    val deckId: Int = checkNotNull(savedStateHandle[CardAddDestination.DECK_ID_ARG])

    private var deckUiState by mutableStateOf(DeckUiState())

    private var _cardsUiState = MutableStateFlow(CardUiState())
    var cardUiState: MutableStateFlow<CardUiState> = _cardsUiState
        private set

    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    init {
        getCards()

        viewModelScope.launch {
            deckUiState = decksRepository.getDeckStream(deckId)
                .filterNotNull()
                .first()
                .toDeckUiState(true)
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

    suspend fun updateDeck(cardId: String) {
        val newListCard: MutableList<String> = deckUiState.deckDetails.cardList.toMutableList()
        newListCard.add(cardId)
        decksRepository.updateDeck(deckUiState.deckDetails.copy(cardList = newListCard.toList()).toDeck())
        val newListOfCards: MutableList<Card> = deckUiState.cards.toMutableList()
        newListOfCards.add(getCardDetails(cardId))
        deckUiState = DeckUiState(
            deckDetails = deckUiState.deckDetails.copy(cardList = newListCard.toList()),
            isEntryValid = true,
            cards = newListOfCards.toList()
        )

    }

    fun updateNameSearch(name: String) {
        cardUiState.value = CardUiState(nameSearch = name, cards = cardUiState.value.cards)
    }

    fun updateCards() {
        getCardByName()
    }


    private fun getCards() {
        viewModelScope.launch{
            try {
                val response = tcgDexApi.getCards()
                _cardsUiState.value = CardUiState(cards = response)
                Log.d("CardsListViewModel", "Cards updated: ${_cardsUiState.value.cards}")

            } catch (e: Exception) {
                // Handle network error
            }
        }
    }

    private fun getCardByName(name: String = cardUiState.value.nameSearch){
        viewModelScope.launch{
            try {
                Log.d("CardsListViewModel", "NAME: $name")

                val response =
                    if(name.isEmpty())
                        tcgDexApi.getCards()
                    else
                        tcgDexApi.getCardsByName(name)

                _cardsUiState.value = CardUiState(
                    cards = response,
                    nameSearch = name
                )
                Log.d("CardsListViewModel", "Cards updated with name: $name, ${_cardsUiState.value.cards}")

            } catch (e: Exception) {
                Log.d("CardsListViewModel", "Error: ${e.message} ")// Handle network error
            }
        }
    }
}