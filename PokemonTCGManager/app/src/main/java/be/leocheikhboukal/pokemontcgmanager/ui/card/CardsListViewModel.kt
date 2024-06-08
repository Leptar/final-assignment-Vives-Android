package be.leocheikhboukal.pokemontcgmanager.ui.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.CardBrief
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.data.user.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardsListViewModel(
    savedStateHandle: SavedStateHandle,
    private val usersRepository: UserRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[CardsListDestination.USER_ID_ARG])

    val apiTcgdex: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.tcgdex.net/v2/en/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    val assetsTcgdex: Retrofit = Retrofit
//        .Builder()
//        .baseUrl("https://assets.tcgdex.net/v2/en/")
//        .build()

    val tcgdexApi: TcgdexApi = apiTcgdex.create(TcgdexApi::class.java)

    val cardsLiveData = MutableLiveData<List<CardBrief>>()

    init {
        getCards()
    }
    fun getCards() {
        viewModelScope.launch{
            try {
                val response = tcgdexApi.getCards()
                val cards: List<CardBrief> = response
                cardsLiveData.postValue(cards)

            } catch (e: Exception) {
                // Handle network error
            }
        }
    }
}

