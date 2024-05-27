package be.howest.ti.taskifox.ui.uistates

import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo
import java.time.ZonedDateTime

data class ModifyTodoUIState(
    val todo: Todo = Todo(
        0, "", "", ZonedDateTime.now().plusDays(1), null),
    val projects: List<Project> = listOf(),
    val isTodoValid: Boolean = false
)