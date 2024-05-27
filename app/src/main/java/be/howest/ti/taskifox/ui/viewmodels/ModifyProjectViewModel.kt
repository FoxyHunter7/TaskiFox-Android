package be.howest.ti.taskifox.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.ti.taskifox.data.repositories.ProjectsRepository
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.ui.uistates.ModifyProjectUIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ModifyProjectViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository
) : ViewModel() {
    private val _modifyProjectUIState = MutableStateFlow(ModifyProjectUIState())
    private val _toastMessages = MutableSharedFlow<String>()
    val modifyProjectUIState = _modifyProjectUIState.asStateFlow()
    val toastMessages = _toastMessages.asSharedFlow()

    private val projectId: Long? = savedStateHandle["projectId"]

    init {
        viewModelScope.launch {
            if (projectId != null) {
                _modifyProjectUIState.value = ModifyProjectUIState(
                    project = projectsRepository.getProject(projectId).first() ?: Project(0, "", "", ""),
                    isProjectValid = true
                )
            }
        }
    }

    fun updateUIState(project: Project) {
        _modifyProjectUIState.value = ModifyProjectUIState(
            project = project,
            isProjectValid = validateInput(project)
        )
    }

    private fun validateInput(project: Project = modifyProjectUIState.value.project): Boolean {
        return with(project) {
            title.isNotBlank() && color.isNotBlank() && color.matches(Regex("^#[a-fA-F0-9]{6}$"))
        }
    }

    suspend fun saveProject() {
        if (validateInput()) {
            if (projectId != null) {
                projectsRepository.update(modifyProjectUIState.value.project)
            } else {
                projectsRepository.insert(modifyProjectUIState.value.project)
            }
        } else {
            viewModelScope.launch {
                _toastMessages.emit("Invalid fields, cannot save, make sure a project at least has a title and valid color hex: #xxxxxx")
            }
        }
    }
}