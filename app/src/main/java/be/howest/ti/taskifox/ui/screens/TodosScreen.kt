package be.howest.ti.taskifox.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import be.howest.ti.taskifox.ui.viewmodels.TodosViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import be.howest.ti.taskifox.ui.AppViewModelProvider
import be.howest.ti.taskifox.ui.components.Day
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(
    navigateToCreateTodo: () -> Unit,
    navigateToEditTodo: (Long) -> Unit,
    viewModel: TodosViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val todosUIState by viewModel.todosUIState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.toastMessages.collect { message ->
            coroutineScope.launch {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateTodo
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Todo")
            }
        },
        content = { innerPadding ->
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
                items(todosUIState.todosByDate.entries.toList()) { (date, todos) ->
                    val weather = viewModel.weatherByDate[date]
                    Day(
                        date = date,
                        todos = todos,
                        weather = weather,
                        onTodoDelete = { todo -> coroutineScope.launch {
                            viewModel.deleteTodo(todo)
                        } },
                        onTodoEdit = navigateToEditTodo
                    )
                }
            }
        }
    )
}

