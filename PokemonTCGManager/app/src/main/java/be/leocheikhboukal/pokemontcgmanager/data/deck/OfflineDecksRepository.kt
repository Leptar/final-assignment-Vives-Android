package be.leocheikhboukal.pokemontcgmanager.data.deck

import kotlinx.coroutines.flow.Flow

class OfflineDecksRepository(private val deckDao: DeckDao) : DecksRepository{
    override fun getAllDecksStream(): Flow<List<Deck>> = deckDao.getAllDecks()

    override fun getDeckStream(id: Int): Flow<Deck?> = deckDao.getDeck(id)

    override suspend fun insertDeck(deck: Deck) = deckDao.insert(deck)

    override suspend fun deleteDeck(deck: Deck) = deckDao.delete(deck)

    override suspend fun updateDeck(deck: Deck) = deckDao.update(deck)
}