package be.howest.ti.taskifox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import be.howest.ti.taskifox.ui.AppViewModelProvider
import be.howest.ti.taskifox.ui.components.Dialog
import be.howest.ti.taskifox.ui.components.Todo
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import be.howest.ti.taskifox.ui.viewmodels.ProjectDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun ProjectDetailsScreen(
    navigateToEditProject: (Long) -> Unit,
    navigateToEditTodo: (Long) -> Unit,
    navigateToProjects: () -> Unit,
    viewModel: ProjectDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val projectDetailsUIState by viewModel.projectDetailsUIState.collectAsState()
    val projectDetailsUIStateProject by viewModel.projectDetailsUIStateProject.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val projectColor = if (projectDetailsUIStateProject.project.color != "") {
        Color(projectDetailsUIStateProject.project.color.toColorInt())
    } else {
        Color.Transparent
    }
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        Dialog(
            title = "Delete Project",
            message = "Are you sure you want to delete this project? This will only delete the project, not the todos in it.",
            confirmBtnTxt = "Yes",
            cancelBtnTxt = "No",
            onConfirm = { coroutineScope.launch {
                viewModel.deleteProject()
                navigateToProjects()
            } },
            onCancel = { showDialog.value = false }
        )
    }

    Column {
        TaskiFoxTheme(darkTheme = projectColor.luminance() < 0.5f) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(projectColor)
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        projectDetailsUIStateProject.project.title,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { navigateToEditProject(projectDetailsUIStateProject.project.id) }
                    ) {
                        Icon(Icons.Default.Edit,
                            contentDescription = "Edit Project",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(
                        onClick = { showDialog.value = true }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Project",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Text(
                    projectDetailsUIStateProject.project.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        LazyColumn(
            modifier = Modifier.padding(20.dp)
        ) {
            items(projectDetailsUIState.projectTodos) { todo ->
                Todo(
                    todo = todo,
                    onDelete = { coroutineScope.launch {
                        viewModel.deleteTodo(todo)
                    } },
                    onEdit = navigateToEditTodo
                )
            }
        }
    }
}