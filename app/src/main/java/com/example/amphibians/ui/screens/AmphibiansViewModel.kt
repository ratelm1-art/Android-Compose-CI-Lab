
package com.example.amphibians.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.AmphibiansApplication
import com.example.amphibians.data.AmphibiansRepository
import com.example.amphibians.model.Amphibian
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AmphibiansUiState {
    data class Success(
        val amphibians: List<Amphibian>
    ) : AmphibiansUiState

    data object Loading : AmphibiansUiState

    data class Error(
        val message: String
    ) : AmphibiansUiState
}

class AmphibiansViewModel(
    private val amphibiansRepository: AmphibiansRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AmphibiansUiState>(
            AmphibiansUiState.Loading
        )

    val uiState: StateFlow<AmphibiansUiState> =
        _uiState.asStateFlow()

    init {
        getAmphibians()
    }

    fun getAmphibians() {
        viewModelScope.launch {
            _uiState.value = AmphibiansUiState.Loading

            try {
                val amphibians =
                    amphibiansRepository.getAmphibians()

                _uiState.value =
                    AmphibiansUiState.Success(amphibians)

            } catch (e: Exception) {
                _uiState.value = AmphibiansUiState.Error(
                    e.localizedMessage
                        ?: "Unable to load amphibians. Please try again."
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[APPLICATION_KEY] as AmphibiansApplication

                AmphibiansViewModel(
                    amphibiansRepository =
                        application.container.amphibiansRepository
                )
            }
        }
    }
}
