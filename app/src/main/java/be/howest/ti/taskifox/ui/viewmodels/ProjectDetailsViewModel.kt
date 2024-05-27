package be.howest.ti.taskifox.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.ti.taskifox.data.repositories.ProjectsRepository
import be.howest.ti.taskifox.data.repositories.TodosRepository
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.ui.uistates.ProjectDetailsUIState
import be.howest.ti.taskifox.ui.uistates.ProjectDetailsUIStateProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProjectDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val todosRepository: TodosRepository
) : ViewModel() {
    private val _projectDetailsUIState = MutableStateFlow(ProjectDetailsUIState())
    private val _projectDetailUISTateProject = MutableStateFlow(ProjectDetailsUIStateProject())
    private val projectId: Long = checkNotNull(savedStateHandle["projectId"])

    val projectDetailsUIState: StateFlow<ProjectDetailsUIState> =
        todosRepository.getAllTodosOfProject(projectId).map { ProjectDetailsUIState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProjectDetailsUIState()
            )

    val projectDetailsUIStateProject: StateFlow<ProjectDetailsUIStateProject> =
        projectsRepository.getProject(projectId).map { project ->
            project?.let { ProjectDetailsUIStateProject(it) }
                ?: ProjectDetailsUIStateProject(Project(0, "","", ""))
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProjectDetailsUIStateProject()
            )

    suspend fun deleteProject() {
        projectsRepository.delete(projectDetailsUIStateProject.value.project.copy())
    }

    suspend fun deleteTodo(todo: Todo) {
        todosRepository.delete(todo)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}