package be.leocheikhboukal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.leocheikhboukal.pokemontcgmanager.data.PTCGManagerDb
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.deck.DeckDao
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.data.user.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DbDaoTest {

    private lateinit var deckDao: DeckDao
    private lateinit var userDao: UserDao
    private lateinit var pokemonTCGManagerDb: PTCGManagerDb
    private val user1 = User(1, "Leptar", null)
    private val user2 = User(2, "Youna", null)
    private val deck1 = Deck(1, "Fun", "deck non competitif, pour les copaing", null,listOf(), 1)
    private val deck2 = Deck(2, "Try hard", "volonté de nuire", null, listOf(), 1)
    private val MissmatchDeck = Deck(3, "Try hard", "volonté de nuire", null, listOf(), 9)


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        pokemonTCGManagerDb = Room.inMemoryDatabaseBuilder(context, PTCGManagerDb::class.java)
            .allowMainThreadQueries()
            .build()
        deckDao = pokemonTCGManagerDb.deckDao()
        userDao = pokemonTCGManagerDb.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        pokemonTCGManagerDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsUserIntoDB() = runBlocking {
        addOneUserToDb()
        val allUsers = userDao.getAllUser().first()
        assertEquals(allUsers[0], user1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsDeckIntoDB() = runBlocking {
        addOneUserToDb()
        addOneDeckToDb()
        val allDecks = deckDao.getAllDecks().first()
        assertEquals(allDecks[0], deck1)

        try {
            deckDao.insert(MissmatchDeck)

            fail("Missmatch deck test fail : insert method succeed ")
        } catch (_ : Exception) {

        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllUser_returnsAllUsersFromDB() = runBlocking {
        addTwoUserToDb()
        val allUsers = userDao.getAllUser().first()
        assertEquals(allUsers[0], user1)
        assertEquals(allUsers[1], user2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllDeck_returnsAllDecksFromDB() = runBlocking {
        addOneUserToDb()
        addTwoDeckToDb()
        val allDecks = deckDao.getAllDecks().first()
        assertEquals(allDecks[0], deck1)
        assertEquals(allDecks[1], deck2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetUser_returnsUserFromDB() = runBlocking {
        addOneUserToDb()
        val user = userDao.getUser(1)
        assertEquals(user.first(), user1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetDeck_returnsDeckFromDB() = runBlocking {
        addOneUserToDb()
        addOneDeckToDb()
        val deck = deckDao.getDeck(1)
        assertEquals(deck.first(), deck1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteUsers_deletesAllUsersFromDB() = runBlocking {
        addTwoUserToDb()
        addTwoDeckToDb()
        userDao.delete(user2)
        userDao.delete(user1)
        val allItems = userDao.getAllUser().first()
        val allDecks = deckDao.getAllDecks().first()
        Assert.assertTrue(allItems.isEmpty())
        Assert.assertTrue(allDecks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteDeck_deletesAllDecksFromDB() = runBlocking {
        addTwoUserToDb()
        addTwoDeckToDb()
        deckDao.delete(deck1)
        deckDao.delete(deck2)
        val allDecks = deckDao.getAllDecks().first()
        Assert.assertTrue(allDecks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateUsers_updatesUsersInDB() = runBlocking {
        addTwoUserToDb()
        userDao.update(User(1, "Parench", null))
        userDao.update(User(2, "Naomie", null))

        val allUsers = userDao.getAllUser().first()
        assertEquals(allUsers[0], User(1, "Parench", null))
        assertEquals(allUsers[1], User(2, "Naomie", null))
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateDecks_updatesDecksInDB() = runBlocking {
        addTwoUserToDb()
        addTwoDeckToDb()
        deckDao.update(Deck(1, "fun/serieux", "mode serieux avec les copaings", null,listOf(), 1))
        deckDao.update(Deck(2, "Try hard", "volonté de nuire", null,listOf(), 2))

        val allDecks = deckDao.getAllDecks().first()
        assertEquals(allDecks[1], Deck(1, "fun/serieux", "mode serieux avec les copaings", null,listOf(), 1))
        assertEquals(allDecks[0], Deck(2, "Try hard", "volonté de nuire", null,listOf(), 2))
    }


    private suspend fun addOneDeckToDb() {
        deckDao.insert(deck1)
    }

    private suspend fun addTwoDeckToDb() {
        deckDao.insert(deck1)
        deckDao.insert(deck2)
    }

    private suspend fun addOneUserToDb() {
        userDao.insert(user1)
    }

    private suspend fun addTwoUserToDb() {
        userDao.insert(user1)
        userDao.insert(user2)
    }

}