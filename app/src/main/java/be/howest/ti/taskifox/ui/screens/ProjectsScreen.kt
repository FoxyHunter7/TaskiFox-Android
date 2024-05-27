package be.howest.ti.taskifox.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.howest.ti.taskifox.ui.AppViewModelProvider
import be.howest.ti.taskifox.ui.components.Project
import be.howest.ti.taskifox.ui.viewmodels.ProjectsViewModel

@Composable
fun ProjectsScreen(
    navigateToCreateProject: () -> Unit,
    navigateToProjectDetail: (Long) -> Unit,
    viewModel: ProjectsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val projectsUIState by viewModel.projectsUIState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateProject
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Project")
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                items(projectsUIState.projects) { project ->
                    Project(
                        project = project,
                        onProjectClick = navigateToProjectDetail
                    )
                }
            }
        }
    )
}