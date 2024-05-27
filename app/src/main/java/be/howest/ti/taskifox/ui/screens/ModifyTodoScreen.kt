package be.howest.ti.taskifox.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.ui.AppViewModelProvider
import be.howest.ti.taskifox.ui.components.DateTimePicker
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import be.howest.ti.taskifox.ui.viewmodels.ModifyTodoViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTodoScreen(
    navigateBack: () -> Unit,
    viewModel: ModifyTodoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val modifyTodoUIState by viewModel.modifyTodoUIState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    
    LaunchedEffect(key1 = viewModel) {
        viewModel.toastMessages.collect { message ->
            coroutineScope.launch {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(
            value = modifyTodoUIState.todo.title,
            onValueChange = { viewModel.updateUIState(modifyTodoUIState.todo.copy(title = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Title") },
            supportingText = { Text("*Required") },
            enabled = true,
            singleLine = true,
            isError = modifyTodoUIState.todo.title.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = modifyTodoUIState.todo.description,
            onValueChange = { viewModel.updateUIState(modifyTodoUIState.todo.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Description") },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
        )
        DateTimePicker(
            dateTime = modifyTodoUIState.todo.dueDateTime,
            onDateTimeChange = { viewModel.updateUIState(modifyTodoUIState.todo.copy(dueDateTime = it)) },
            allowedYearRange = Year.now().value..Year.now().value + 5,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val currentDate = LocalDate.now()
                    val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !selectedDate.isBefore(currentDate)
                }
            },
            dateRequired = true
        )
        OutlinedTextField(
            value = modifyTodoUIState.todo.projectId?.toString() ?: "",
            onValueChange = {
                val idLong = if (it.isNotBlank()) {
                    it.toLong()
                } else {
                    null
                }

                viewModel.updateUIState(modifyTodoUIState.todo.copy(
                    projectId = idLong
                ))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Project") },
            supportingText = {
                if (modifyTodoUIState.todo.projectId != null) {
                    val text = modifyTodoUIState.projects.firstOrNull { it.id == modifyTodoUIState.todo.projectId }
                        ?.title
                        ?: "No project found with that id"
                    Text(text)
                }
            },
            enabled = true,
            isError = modifyTodoUIState.todo.projectId != null
                    && !modifyTodoUIState.projects.any { it.id == modifyTodoUIState.todo.projectId },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navigateBack() }
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveTodo()
                        navigateBack()
                    }
                },
                enabled = modifyTodoUIState.isTodoValid,
                modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Text("Confirm")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewModifyTodoScreen() {
    TaskiFoxTheme {
        ModifyTodoScreen(navigateBack = {})
    }
}