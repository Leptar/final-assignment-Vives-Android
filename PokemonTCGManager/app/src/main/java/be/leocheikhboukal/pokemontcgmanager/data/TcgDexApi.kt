package be.leocheikhboukal.pokemontcgmanager.data

import retrofit2.http.GET

data class ApiResponseforListCards(
    val content: List<CardBrief>
)

data class CardBrief(
    val id: String,
    val localId: String,
    val name: String,
    val image: String?,
)

interface TcgdexApi {

    @GET("cards")
    suspend fun getCards(): ApiResponseforListCards

}
