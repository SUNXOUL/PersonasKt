package com.sagrd.personas.ui.person

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.sagrd.personas.Nav.AppScreens
import kotlinx.coroutines.flow.collectLatest
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsConsultScreen(
    viewModel: PersonViewModel = hiltViewModel(),
    navController: NavController
)
{
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Persona Eliminada",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }


    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text(text = "PERSONAS") },
            modifier = Modifier.shadow(8.dp),
            colors =TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            navigationIcon = {
                Icon(imageVector = Icons.Filled.Face, contentDescription ="" )
            },
            actions = {
                IconButton(onClick = { navController.navigate(route = AppScreens.OccupationsScreen.route) }) {
                    Icon(imageVector = Icons.Filled.Work, contentDescription ="" )
                }
            }
        )
        },
        content = ({
            PersonConsult(viewModel)
        }),
        bottomBar = {
            Row (horizontalArrangement = Arrangement.End, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()){
                FloatingActionButton(onClick = { navController.navigate(route = AppScreens.FormScreen.route) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription ="Add" )
                }
            }
        }
    )

}
@Composable
fun PersonConsult(
    personViewModel: PersonViewModel = hiltViewModel(),
    occupationViewModel: OccupationViewModel = hiltViewModel()
)
{
    val persons by personViewModel.persons.collectAsStateWithLifecycle()
    val occupations by occupationViewModel.Occupations.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp)
    ) {
        items(persons) { person ->

            Card(modifier = Modifier
                .padding(10.dp)
                .shadow(3.dp),
            ) {
                Box()
                {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = {
                            person.personId?.let { personViewModel.DeletePerson(personId = it) }
                            personViewModel.setMessageShown()
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription ="" )
                        }
                    }
                    Column {
                        Column (horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth())
                        {
                            RoundImage(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(10.dp),
                                image = Uri.parse(person.image),
                                Description = "Profile Photo"
                            )
                            Text(text = "${person.name}")
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Divider()
                        Row {
                            Column {
                                Text(text = """
                            Telefono
                            Celular
                            Email 
                            Direccion 
                            Fecha de nacimiento
                            Ocupacion   
                    """.trimIndent(),modifier = Modifier.padding(10.dp))
                            }
                            Column {
                                Text(text = """
                            :  ${person.telephone}
                            :  ${person.cellphone}
                            :  ${person.email}
                            :  ${person.direccion}
                            :  ${person.fechaNacimiento}
                            :  ${occupations.singleOrNull(){ ocupation-> ocupation.ocupationId==person.ocupationId}?.name}    
                    """.trimIndent(),modifier = Modifier.padding(10.dp))
                            }
                        }
                    }

                }

           }
        }
    }
}
@Composable
fun RoundImage(
    image: Uri,
    modifier: Modifier = Modifier,
    Description: String?
)
{
    AsyncImage(model = image,
        contentDescription =Description,
        modifier= modifier
            .border(
                width = 5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .clip(CircleShape)
            .aspectRatio(1f, true)
    )
}
