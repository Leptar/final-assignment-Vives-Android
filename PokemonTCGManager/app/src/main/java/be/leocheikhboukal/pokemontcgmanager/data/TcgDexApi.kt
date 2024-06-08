package be.leocheikhboukal.pokemontcgmanager.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Card(
    val id: String,
    val localId: Any, // Can be String or Number
    val name: String,
    val image: String?,
    val category: String,
    val illustrator: String?,
    val rarity: String?,
    val variants: Variants,
    val set: SetBrief,
    val dexId: List<Int>?,
    val hp: Int?,
    val types: List<String>?,
    val evolveFrom: String?,
    val description: String?,
    val level: String?,
    val stage: String?,
    val suffix: String?,
    val item: Item?,
    val effect: String?,
    val trainerType: String?,
    val energyType: String?
)

data class Item(
    val name: String?,
    val effect: String?
)

data class SetBrief(
    val id: String,
    val name: String,
    val logo: String?,
    val symbol: String?,
    val cardCount: CardCount
)

data class CardCount(
    val total: Int,
    val official: Int
)
data class Variants(
    val normal: Boolean,
    val reverse: Boolean,
    val holo: Boolean,
    val firstEdition: Boolean
)

data class CardBrief(
    val id: String,
    val localId: String,
    val name: String,
    val image: String?,
)

interface TcgdexApi {

    @GET("cards")
    suspend fun getCards(): List<CardBrief>

    @GET("cards/{id}")
    suspend fun getCardDetails(@Path("id")id: String): Card

    @GET("cards?name=name")
    suspend fun getCardsByName(@Query("name")name: String): List<CardBrief>
}
