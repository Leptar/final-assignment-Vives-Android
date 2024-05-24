package be.leocheikhboukal.pokemontcgmanager.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from Users")
    fun getAllUser(): Flow<List<User>>

    @Transaction
    @Query("SELECT * from Users WHERE id = :id")
    fun getUserAndDeck(id: Int): Flow<userWithDecks>

    @Query("SELECT * FROM Users WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}