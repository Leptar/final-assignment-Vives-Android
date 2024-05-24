package be.leocheikhboukal.pokemontcgmanager.data.deck

import kotlinx.coroutines.flow.Flow


interface DecksRepository {

    fun getAllDecksStream(): Flow<List<Deck>>

    fun getDeckStream(id: Int): Flow<Deck?>

    suspend fun insertDeck(deck: Deck)

    suspend fun deleteDeck(deck: Deck)

    suspend fun updateDeck(deck: Deck)
}