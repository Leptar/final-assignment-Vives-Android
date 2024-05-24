package be.leocheikhboukal.pokemontcgmanager.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null
)
