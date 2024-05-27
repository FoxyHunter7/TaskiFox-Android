package be.howest.ti.taskifox.ui.uistates

import be.howest.ti.taskifox.models.TodoWithProjectColor
import java.time.LocalDate

data class TodosUIState(
    val todosByDate: Map<LocalDate, List<TodoWithProjectColor>> = mapOf()
)