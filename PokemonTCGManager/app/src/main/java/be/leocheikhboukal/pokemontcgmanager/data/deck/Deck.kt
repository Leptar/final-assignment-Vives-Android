package be.leocheikhboukal.pokemontcgmanager.data.deck


import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import be.leocheikhboukal.pokemontcgmanager.data.user.User

@Entity(
    tableName = "Decks",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val cardList: List<String>,
    val color: Int,
    val userId: Int
)
