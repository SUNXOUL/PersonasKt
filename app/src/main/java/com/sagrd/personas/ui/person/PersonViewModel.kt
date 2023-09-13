package com.sagrd.personas.ui.person

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.personas.data.PersonDB
import com.sagrd.personas.domain.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personDB: PersonDB
) : ViewModel() {
    var name : String by mutableStateOf("")
    var nameError by mutableStateOf (true)
    var ocupationId : Int by mutableStateOf (0)
    var ocupationError by mutableStateOf (true)
    var image by mutableStateOf<Uri?> (null)
    var telephone: String by mutableStateOf("")
    var telephoneError by mutableStateOf (true)
    var cellphone: String by mutableStateOf("")
    var cellphoneError by mutableStateOf (true)
    var email: String by mutableStateOf("")
    var emailError by mutableStateOf (true)
    var fechaNacimiento: String by mutableStateOf("")
    var fechaError by mutableStateOf (true)
    var direccion: String by mutableStateOf("")
    var direccionError by mutableStateOf (true)

    fun onNameChanged(valor: String) {
        name = valor
        nameError = valor.isBlank();
    }
    fun onOcupationChanged(valor: Int?) {
        if (valor != null) {
            ocupationId = valor
            ocupationError = (valor<=0)
        }
    }
    fun onTelephoneChanged(valor: String) {
        telephone = valor
        telephoneError = valor.isBlank();
    }
    fun onCellphoneChanged(valor: String) {
        cellphone = valor
        cellphoneError = valor.isBlank();
    }
    fun onEmailChanged(valor: String) {
        email = valor
        emailError = valor.isBlank();
    }
    fun onFechaChanged(valor: String) {
        fechaNacimiento = valor
        fechaError = valor.isBlank();
    }
    fun onDireccionChanged(valor: String) {
        direccion = valor
        direccionError = valor.isBlank();
    }

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

    val persons: StateFlow<List<Person>> = personDB.personDao().getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun SavePerson() {
        viewModelScope.launch {
            val person: Person = Person(
                name = name,
                ocupationId = this@PersonViewModel.ocupationId,
                image = image.toString(),
                cellphone = cellphone,
                telephone = telephone,
                email = email,
                direccion = direccion,
                fechaNacimiento = fechaNacimiento

            )
            personDB.personDao().save(person)
            Clear()
        }
    }
    fun DeletePerson(
        personId : Int
    ) {
        viewModelScope.launch {
            val person = personDB.personDao().find(personId)
            personDB.personDao().delete(person)
            Clear()
        }
    }

    private fun Clear() {
        name = ""
        cellphone=""
        telephone=""
        email=""
        direccion=""
        fechaNacimiento=""
        ocupationId=0
        image=Uri.EMPTY
    }
}