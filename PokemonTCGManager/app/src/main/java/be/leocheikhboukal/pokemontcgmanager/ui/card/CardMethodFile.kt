package be.leocheikhboukal.pokemontcgmanager.ui.card

import android.util.Log
import be.leocheikhboukal.pokemontcgmanager.data.Card
import be.leocheikhboukal.pokemontcgmanager.data.CardBrief
import be.leocheikhboukal.pokemontcgmanager.data.CardCount
import be.leocheikhboukal.pokemontcgmanager.data.SetBrief
import be.leocheikhboukal.pokemontcgmanager.data.TcgdexApi
import be.leocheikhboukal.pokemontcgmanager.data.Variants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

suspend fun getCardDetails(tcgDexApiParam: TcgdexApi, id: String): Card {

    return try {
        tcgDexApiParam.getCardDetails(id)

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

fun updateNameSearch(cardUiState: MutableStateFlow<CardUiState>, name: String) {
    cardUiState.value = CardUiState(nameSearch = name, cards = cardUiState.value.cards)
}

data class CardUiState(
    var cards: List<CardBrief> = emptyList(),
    var nameSearch: String = "",
)



fun getCardByName(
    viewModelScope: CoroutineScope,
    tcgDexApi: TcgdexApi,
    name: String,
    cardsUiState: MutableStateFlow<CardUiState>
){
    viewModelScope.launch{
        try {

            val response =
                if(name.isEmpty())
                    tcgDexApi.getCards()
                else
                    tcgDexApi.getCardsByName(name)

            cardsUiState.value = CardUiState(
                cards = response,
                nameSearch = name
            )

        } catch (e: Exception) {
            Log.d("CardsListViewModel", "Error: ${e.message} ")// Handle network error
        }
    }
}

fun getCards(
    viewModelScope: CoroutineScope,
    tcgDexApi: TcgdexApi,
    cardsUiState: MutableStateFlow<CardUiState>
) {
    viewModelScope.launch{
        try {
            val response = tcgDexApi.getCards()
            cardsUiState.value = CardUiState(cards = response)

        } catch (e: Exception) {
            // Handle network error
        }
    }
}