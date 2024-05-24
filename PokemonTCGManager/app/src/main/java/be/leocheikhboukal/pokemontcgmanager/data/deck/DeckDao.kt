package be.leocheikhboukal.pokemontcgmanager.data.deck

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Query("SELECT * FROM Decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<Deck>>

    @Query("Select * FROM Decks WHERE id = :id")
    fun getDeck(id: Int): Flow<Deck>

    @Insert
    suspend fun insert(deck: Deck)

    @Update
    suspend fun update(deck: Deck)

    @Delete
    suspend fun delete(deck: Deck)
}