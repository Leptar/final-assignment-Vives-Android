package be.leocheikhboukal.pokemontcgmanager.data

import androidx.room.TypeConverter

/**
 * Classe that allow to save complex type like date, list or Array
 */
class Converters {

    @TypeConverter
    fun fromListToString(listIdCard: List<String>): String = listIdCard.toString()

    @TypeConverter
    fun fromStringToList(listIdCardInString: String): List<String> =
        listIdCardInString
            .replace("[","")
            .replace("]","")
            .replace(" ","")
            .split(",")

}