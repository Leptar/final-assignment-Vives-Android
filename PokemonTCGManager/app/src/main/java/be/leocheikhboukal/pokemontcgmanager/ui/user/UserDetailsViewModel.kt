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
}

