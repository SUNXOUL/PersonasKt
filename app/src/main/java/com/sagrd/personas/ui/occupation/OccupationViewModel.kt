package com.sagrd.personas.ui.person

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.personas.data.PersonDB
import com.sagrd.personas.domain.model.Ocupation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccupationViewModel @Inject constructor(
    private val personDB: PersonDB
) : ViewModel() {
    var name by mutableStateOf ("")
    var nameError by mutableStateOf (true)
    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }
    fun onNameChanged(valor: String) {
        name = valor
        nameError = valor.isBlank();
    }

    val Occupations: StateFlow<List<Ocupation>> = personDB.occupationDao().getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun SaveOccupation() {
        viewModelScope.launch {
            val ocupation: Ocupation = Ocupation(
                name = name
            )
            if (nameError !=true)
            {
                personDB.occupationDao().save(ocupation)
                Clear()
            }

        }
    }
    fun DeleteOccupation() {
        viewModelScope.launch {
            val ocupation: Ocupation = Ocupation(
                name = name,
            )
            personDB.occupationDao().delete(ocupation)
            Clear()
        }
    }

    private fun Clear() {
        name = ""
    }
}