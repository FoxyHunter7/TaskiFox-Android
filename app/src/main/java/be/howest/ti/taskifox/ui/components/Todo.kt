package be.howest.ti.taskifox.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import be.howest.ti.taskifox.models.ITodo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import java.time.ZonedDateTime

@Composable
fun Todo(
    todo: ITodo,
    onDelete: (ITodo) -> Unit,
    onEdit: (Long) -> Unit,
    ) {
    val backgroundColor = if (todo is TodoWithProjectColor && !todo.projectColor.isNullOrEmpty()) {
        Color(todo.projectColor.toColorInt())
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val isExpanded = rememberSaveable { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        Dialog(
            title = "Complete Task",
            message = "Are you sure you want to complete this task? Done is Gone!",
            confirmBtnTxt = "Yes",
            cancelBtnTxt = "No",
            onConfirm = { onDelete(todo) },
            onCancel = { showDialog.value = false }
        )
    }

    TaskiFoxTheme(darkTheme = backgroundColor.luminance() < 0.5f) {
        Card(
            onClick = { isExpanded.value = !isExpanded.value }
        ) {
            Row(
                modifier = Modifier
                    .background(backgroundColor.copy(alpha = 0.8f))
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.height(25.dp)
                ) {
                    Icon(
                        Icons.Default.CheckBoxOutlineBlank,
                        "Complete Todo",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                DueTimeDisplay(todo.dueDateTime, backgroundColor)
                Text(
                    todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onEdit(todo.id) },
                    modifier = Modifier.height(25.dp),
                ) {
                    Icon(
                        Icons.Default.Edit,
                        "Edit Todo",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            AnimatedVisibility(visible = isExpanded.value) {
                Box(
                    modifier = Modifier
                        .background(backgroundColor.copy(alpha = 0.8f))
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoPreview() {
    TaskiFoxTheme {
        Todo(
            todo = TodoWithProjectColor(
                1,
                "I'm a todo",
                "And this is my description, amazing right, truly unbelievable",
                ZonedDateTime.now(),
                1,
                projectColor = "#FFAA00"),
            onDelete = {},
            onEdit = {}
        )
    }
}
