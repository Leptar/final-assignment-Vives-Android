package be.leocheikhboukal.pokemontcgmanager.ui.card.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.ui.card.CardUiState
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCardByName
import be.leocheikhboukal.pokemontcgmanager.ui.card.getCards
import be.leocheikhboukal.pokemontcgmanager.ui.card.screen.CardsListDestination
import kotlinx.coroutines.flow.MutableStateFlow
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
        getCards(
            viewModelScope = viewModelScope,
            tcgDexApi = tcgDexApi,
            cardsUiState = _cardsUiState
        )
    }



    fun updateCards() {
        getCardByName(
            viewModelScope = viewModelScope,
            tcgDexApi = tcgDexApi,
            name = cardUiState.value.nameSearch,
            cardsUiState = _cardsUiState
        )
    }

    fun updateNameSearch(name: String) {
        be.leocheikhboukal.pokemontcgmanager.ui.card.updateNameSearch(cardUiState, name)
    }
}




