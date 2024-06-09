package be.leocheikhboukal.pokemontcgmanager.ui.card.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.ui.card.screen.CardDetailsDestination
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cardId: String = checkNotNull(savedStateHandle[CardDetailsDestination.CARD_ID_ARG])
    val userId: Int = checkNotNull(savedStateHandle[CardDetailsDestination.USER_ID_ARG])

    private val apiTcgDex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tcgDexApi: TcgdexApi = apiTcgDex.create(TcgdexApi::class.java)

    val cardsLiveData = MutableLiveData<Card>()

    init {
        getCardDetails()
    }

    private fun getCardDetails() {
        viewModelScope.launch{
            try {
                val response = tcgDexApi.getCardDetails(cardId)
                val card: Card = response
                cardsLiveData.postValue(card)

            } catch (e: Exception) {
                // Handle network error
            }
        }
    }
}