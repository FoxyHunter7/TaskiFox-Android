package be.howest.ti.taskifox.ui.viewmodels

import androidx.lifecycle.ViewModel
import be.howest.ti.taskifox.data.repositories.ProjectsRepository
import be.howest.ti.taskifox.ui.uistates.ProjectsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

class ProjectsViewModel(
    private val projectsRepository: ProjectsRepository
) : ViewModel() {
    private val _projectsUIState = MutableStateFlow(ProjectsUIState())

    val projectsUIState: StateFlow<ProjectsUIState> =
        projectsRepository.getAllProjectsStream().map { ProjectsUIState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProjectsUIState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}