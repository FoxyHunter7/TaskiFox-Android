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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.howest.ti.taskifox.ui.AppViewModelProvider
import be.howest.ti.taskifox.ui.viewmodels.ModifyProjectViewModel
import kotlinx.coroutines.launch

@Composable
fun ModifyProjectScreen(
    navigateBack: () -> Unit,
    viewModel: ModifyProjectViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val modifyProjectUIState by viewModel.modifyProjectUIState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.toastMessages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(
            value = modifyProjectUIState.project.title,
            onValueChange = { viewModel.updateUIState(modifyProjectUIState.project.copy(title = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Title") },
            supportingText = { Text("*Required") },
            enabled = true,
            singleLine = true,
            isError = modifyProjectUIState.project.title.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = modifyProjectUIState.project.description,
            onValueChange = { viewModel.updateUIState(modifyProjectUIState.project.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Description") },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
        )
        OutlinedTextField(
            value = modifyProjectUIState.project.color,
            onValueChange = { viewModel.updateUIState(modifyProjectUIState.project.copy(color = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Color") },
            supportingText = { Text("*Required (eg: #ffaa00)") },
            enabled = true,
            isError = modifyProjectUIState.project.color.isBlank()
                    || !modifyProjectUIState.project.color.matches(Regex("^#[a-fA-F0-9]{6}$")),
            modifier = Modifier.fillMaxWidth()
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
                        viewModel.saveProject()
                        navigateBack()
                    }
                },
                enabled = modifyProjectUIState.isProjectValid,
                modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Text("Confirm")
            }
        }
    }
}