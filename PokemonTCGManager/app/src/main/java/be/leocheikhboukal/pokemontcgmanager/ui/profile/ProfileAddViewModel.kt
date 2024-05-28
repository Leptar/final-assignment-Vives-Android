package be.leocheikhboukal.pokemontcgmanager.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import be.leocheikhboukal.pokemontcgmanager.data.user.User
import be.leocheikhboukal.pokemontcgmanager.data.user.UserRepository

class ProfileAddViewModel(private val usersRepository: UserRepository) : ViewModel() {
    /**
     * Holds current item ui state
     */
    var profileAddUiState by mutableStateOf(ProfileAddUiState())
        private set

    /**
     * Updates the [ProfileAddUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(profileDetails: ProfileDetails) {
        profileAddUiState =
            ProfileAddUiState(profileDetails = profileDetails)
    }

    /**
     * Inserts an [User] in the Room database
     */
    suspend fun saveUser() {
        usersRepository.insertUser(profileAddUiState.profileDetails.toUser())
    }
}

/**
 * Ui State for ProfileAddScreen
 */
data class ProfileAddUiState(val profileDetails: ProfileDetails = ProfileDetails())

data class ProfileDetails(
    val id: Int = 0,
    val name: String = "",
    val data: ByteArray? = null
) {
    fun toUser(): User {
        return User(id, name, data)
    }
}