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
            ProfileAddUiState(profileDetails = profileDetails, isEntryValid = validateInput(profileDetails))
    }

    /**
     * Inserts an [User] in the Room database
     */
    suspend fun saveUser() {
        if (validateInput()) {
            usersRepository.insertUser(profileAddUiState.profileDetails.toUser())
        }
    }

    private fun validateInput(uiState: ProfileDetails = profileAddUiState.profileDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

/**
 * Ui State for ProfileAddScreen
 */
data class ProfileAddUiState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false
)

data class ProfileDetails(
    val id: Int = 0,
    val name: String = "",
    val color: Int = 1
)

fun ProfileDetails.toUser(): User {
    return User(id, name, color)
}

/**
 * Extension function to convert [User] to [ProfileAddUiState]
 */
fun User.toItemUiState(isEntryValid: Boolean = false): ProfileAddUiState = ProfileAddUiState(
    profileDetails = this.toProfileDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [User] to [ProfileDetails]
 */
fun User.toProfileDetails(): ProfileDetails = ProfileDetails(
    id = id,
    name = name,
    color = color
)
