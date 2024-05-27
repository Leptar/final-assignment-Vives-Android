package be.leocheikhboukal.pokemontcgmanager.data

import android.content.Context
import be.leocheikhboukal.pokemontcgmanager.data.deck.DecksRepository
import be.leocheikhboukal.pokemontcgmanager.data.deck.OfflineDecksRepository
import be.leocheikhboukal.pokemontcgmanager.data.user.OfflineUsersRepository
import be.leocheikhboukal.pokemontcgmanager.data.user.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val usersRepository: UserRepository
    val decksRepository: DecksRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineUsersRepository]
 * and [OfflineDecksRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [UserRepository]
     */
    override val usersRepository: UserRepository by lazy {
        OfflineUsersRepository(PTCGManagerDb.getDatabase(context).userDao())
    }

    /**
     * Implementation for [DecksRepository]
     */
    override val decksRepository: DecksRepository by lazy {
        OfflineDecksRepository(PTCGManagerDb.getDatabase(context).deckDao())
    }
}