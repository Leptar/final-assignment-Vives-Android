package be.leocheikhboukal.pokemontcgmanager.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsersStream(): Flow<List<User>>

    fun getUserAndDecksStream(id: Int): Flow<UserWithDecks?>

    fun getUser(id: Int): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun updateUser(user: User)
}