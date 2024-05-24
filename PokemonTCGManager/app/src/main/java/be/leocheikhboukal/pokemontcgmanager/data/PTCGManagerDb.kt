package be.leocheikhboukal.pokemontcgmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck
import be.leocheikhboukal.pokemontcgmanager.data.deck.DeckDao
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.data.user.UserDao

@Database(entities = [User::class, Deck::class], version = 1)
abstract class PTCGManagerDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun deckDao(): DeckDao

    companion object {
        @Volatile
        private var Instance: PTCGManagerDb? = null

        fun getDatabase(context: Context): PTCGManagerDb {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PTCGManagerDb::class.java, "PTCGManager_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}