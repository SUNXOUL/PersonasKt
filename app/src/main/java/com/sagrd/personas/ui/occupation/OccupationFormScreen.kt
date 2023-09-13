package com.sagrd.personas.ui.occupation

import com.sagrd.personas.ui.person.PersonViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sagrd.personas.Nav.AppScreens
import com.sagrd.personas.ui.person.OccupationViewModel
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OccupationFormScreen(
    occupationViewModel: OccupationViewModel = hiltViewModel(),
    navController: NavController
)
{
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        occupationViewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Ocupacion guardada",
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
                title = { Text(text = "Agregar Ocupacion") },
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
            OccupationForm(navController =navController, occupationViewModel = occupationViewModel)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun OccupationForm(
    navController: NavController,
    occupationViewModel: OccupationViewModel = hiltViewModel(),
) {

    val occupations by occupationViewModel.Occupations.collectAsStateWithLifecycle()


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
                value = occupationViewModel.name,
                onValueChange = { occupationViewModel.onNameChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                isError = occupationViewModel.nameError,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = occupationViewModel.nameError,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                    }
                },
                label = { Text("Ingrese nombre") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                if(!occupationViewModel.nameError) {
                    keyboardController?.hide()
                    occupationViewModel.SaveOccupation()
                }
            }, modifier = Modifier
                .fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Done, contentDescription ="Done" )
            }
        }

    }
}