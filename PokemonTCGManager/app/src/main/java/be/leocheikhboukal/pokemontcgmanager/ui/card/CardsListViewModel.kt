package be.leocheikhboukal.pokemontcgmanager.ui.card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.CardBrief
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardsListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[CardsListDestination.USER_ID_ARG])

    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    private var _cardsUiState = MutableStateFlow(CardUiState())
    var cardUiState: MutableStateFlow<CardUiState> = _cardsUiState
        private set

    init {
        getCards()
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

data class CardUiState(
    var cards: List<CardBrief> = emptyList(),
    var nameSearch: String = "",
)



