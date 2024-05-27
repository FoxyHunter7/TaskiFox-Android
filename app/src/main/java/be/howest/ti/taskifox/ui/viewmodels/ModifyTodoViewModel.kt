package be.howest.ti.taskifox.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.ti.taskifox.data.repositories.ProjectsRepository
import be.howest.ti.taskifox.data.repositories.TodosRepository
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.ui.uistates.ModifyTodoUIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ModifyTodoViewModel(
    savedStateHandle: SavedStateHandle,
    private val todosRepository: TodosRepository,
    private val projectsRepository: ProjectsRepository
) : ViewModel() {
    private val _modifyTodoUIState = MutableStateFlow(ModifyTodoUIState())
    private val _toastMessages = MutableSharedFlow<String>()
    val modifyTodoUIState = _modifyTodoUIState.asStateFlow()
    val toastMessages = _toastMessages.asSharedFlow()

    private val todoId: Long? = savedStateHandle["todoId"]

    init {
        viewModelScope.launch {
            if (todoId != null) {
                _modifyTodoUIState.value = ModifyTodoUIState(
                    todo = getTodo(todoId),
                    projects = getProjects(),
                    isTodoValid = true
                )
            } else {
                _modifyTodoUIState.value = ModifyTodoUIState(
                    projects = getProjects()
                )
            }
        }
    }

    fun updateUIState(todo: Todo) {
        _modifyTodoUIState.value = ModifyTodoUIState(
            todo = todo,
            projects = modifyTodoUIState.value.projects,
            isTodoValid = validateInput(todo)
        )
    }

    private fun validateInput(todo: Todo = modifyTodoUIState.value.todo): Boolean {
        return with(todo) { title.isNotBlank()
                && dueDateTime.month != null
                && (projectId == null || modifyTodoUIState.value.projects.any { it.id == projectId })
        }
    }

    suspend fun saveTodo() {
        if (validateInput()) {
            if (todoId != null) {
                todosRepository.update(modifyTodoUIState.value.todo)
            } else {
                todosRepository.insert(modifyTodoUIState.value.todo)
            }
        } else {
            viewModelScope.launch {
                _toastMessages.emit("Invalid fields, cannot save, make sure a todo at least has a title")
            }
        }
    }

    private suspend fun getProjects(): List<Project> {
        return projectsRepository.getAllProjectsStream().first()
    }

    private suspend fun getTodo(id: Long): Todo {
        return todosRepository.getTodo(id).first()
    }
}