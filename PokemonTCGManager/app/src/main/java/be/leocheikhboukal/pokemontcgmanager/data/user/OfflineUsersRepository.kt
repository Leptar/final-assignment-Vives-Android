package be.leocheikhboukal.pokemontcgmanager.data.user

import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUser()

    override fun getUserAndDecksStream(id: Int): Flow<userWithDecks?> = userDao.getUserAndDeck(id)

    override fun getUser(id: Int): Flow<User> = userDao.getUser(id)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
}