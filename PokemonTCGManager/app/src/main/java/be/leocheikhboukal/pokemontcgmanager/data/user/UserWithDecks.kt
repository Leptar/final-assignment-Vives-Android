package be.leocheikhboukal.pokemontcgmanager.data.user

import androidx.room.Embedded
import androidx.room.Relation
import be.leocheikhboukal.pokemontcgmanager.data.deck.Deck

data class UserWithDecks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    var decks: List<Deck>
)
