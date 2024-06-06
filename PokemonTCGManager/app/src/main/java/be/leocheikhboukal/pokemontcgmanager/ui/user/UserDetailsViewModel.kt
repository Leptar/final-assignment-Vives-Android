package be.leocheikhboukal.pokemontcgmanager.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.leocheikhboukal.pokemontcgmanager.data.user.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set

    private val userId: Int = checkNotNull(savedStateHandle[UserDetailsDestination.USER_ID_ARG])

    init {
       viewModelScope.launch {
           userUiState = userRepository.getUser(userId)
               .filterNotNull()
               .first()
               .toUserUiState(true)
       }
    }

    /**
     * Updates the [UserUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(userDetails: UserDetails) {
        userUiState =
            UserUiState(userDetails = userDetails, isEntryValid = validateInput(userDetails))
    }

    /**
     * Update the user in the [UserRepository]'s data source
     */
    suspend fun updateUser() {
        if (validateInput()) {
            userRepository.updateUser(userUiState.userDetails.toUser())
        }
    }

    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && name.length <= 9 && color > 0 && color < 5
        }
    }
}

