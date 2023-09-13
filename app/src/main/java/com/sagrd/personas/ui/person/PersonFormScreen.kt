package com.sagrd.personas.ui.person

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sagrd.personas.Nav.AppScreens
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonFormScreen(
    viewModel: PersonViewModel = hiltViewModel(),
    navController: NavController,
    context: Context,
)
{
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Persona guardada",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Agregar Persona") },
                modifier = Modifier.shadow(16.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = AppScreens.ConsultScreen.route) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            PersonForm(context=context,navController=navController,personviewModel = viewModel)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PersonForm(
    navController: NavController,
    occupationViewModel: OccupationViewModel = hiltViewModel(),
    personviewModel: PersonViewModel = hiltViewModel(),
    OccupationviewModel: OccupationViewModel = hiltViewModel(),
    context: Context,
) {

    val occupations by occupationViewModel.Occupations.collectAsStateWithLifecycle()

    var photoPickerLauncher =  rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->  personviewModel.image = uri})

    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            OutlinedTextField(
                value = personviewModel.name,
                onValueChange = { personviewModel.onNameChanged(it) },
                isError = personviewModel.nameError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese nombre") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = personviewModel.telephone,
                onValueChange = { personviewModel.onTelephoneChanged(it) },
                isError = personviewModel.telephoneError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese numero de telefono") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = personviewModel.cellphone,
                onValueChange = {personviewModel.onCellphoneChanged(it) },
                isError = personviewModel.cellphoneError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese numero de telefono celular") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = personviewModel.email,
                onValueChange = { personviewModel.onEmailChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                isError = personviewModel.emailError,
                label = { Text("Ingrese Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = personviewModel.direccion,
                onValueChange = { personviewModel.onDireccionChanged(it)  },
                isError = personviewModel.direccionError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ingrese Direccion") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next)
            )
            showDatePicker(context = context,PersonviewModel=personviewModel)
            Spacer(modifier = Modifier.height(15.dp))

            Column {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {  },
                    isError = personviewModel.ocupationError,
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = {Text("Selecciona la ocupacion")},
                    trailingIcon = {
                        Icon(icon,"contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                ) {
                    occupations.forEach { ocupation ->
                        DropdownMenuItem(text = { Text(text = ocupation.name)}, onClick = {
                            personviewModel.onOcupationChanged(ocupation.ocupationId)
                            selectedText=ocupation.name
                            expanded = !expanded
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = personviewModel.image?.lastPathSegment.toString(),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { IconButton(onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Icon(imageVector = Icons.Filled.Image, contentDescription ="imagen" )
                        }
                        },
                        label = { Text("Ingrese Imagen") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next)
                    )
                }
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                keyboardController?.hide()
                    if (!personviewModel.nameError && !personviewModel.telephoneError &&
                        !personviewModel.cellphoneError&& !personviewModel.emailError&&
                        !personviewModel.direccionError&& !personviewModel.fechaError&&
                        !personviewModel.ocupationError)
                    {
                        personviewModel.SavePerson()
                        personviewModel.setMessageShown()
                        selectedText=""
                    }
            }, modifier = Modifier
                .fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Done, contentDescription ="Done" )
            }
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showDatePicker(
    context: Context,
    PersonviewModel: PersonViewModel = hiltViewModel(),
) {

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            PersonviewModel.onFechaChanged( "$dayOfMonth/$month/$year")
        }, year, month, day
    )
    OutlinedTextField(
        value = PersonviewModel.fechaNacimiento,
        onValueChange = { },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        isError = PersonviewModel.fechaError,
        leadingIcon = { IconButton(onClick = {
            datePickerDialog.show()
        }) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription ="date" )
        }
        },
        label = { Text("Ingrese Fecha de Nacimiento") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next)
    )

}