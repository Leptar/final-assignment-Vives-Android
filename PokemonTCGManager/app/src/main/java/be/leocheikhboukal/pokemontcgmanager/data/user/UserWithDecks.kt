package be.leocheikhboukal.pokemontcgmanager.data.user

import androidx.room.Embedded
import androidx.room.Relation
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck

data class userWithDecks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val decks: List<Deck>
)
