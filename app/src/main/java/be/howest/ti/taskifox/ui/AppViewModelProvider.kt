package be.howest.ti.taskifox.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.howest.ti.taskifox.TaskiFoxApplication
import be.howest.ti.taskifox.ui.viewmodels.ModifyProjectViewModel
import be.howest.ti.taskifox.ui.viewmodels.ModifyTodoViewModel
import be.howest.ti.taskifox.ui.viewmodels.ProjectDetailsViewModel
import be.howest.ti.taskifox.ui.viewmodels.ProjectsViewModel
import be.howest.ti.taskifox.ui.viewmodels.SettingsViewModel
import be.howest.ti.taskifox.ui.viewmodels.TodosViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TodosViewModel(
                taskiFoxApplication().container.todosRepository,
                taskiFoxApplication().container.weatherRepository,
                taskiFoxApplication().container.settingsRepository
            )
        }

        initializer {
            ModifyTodoViewModel(
                this.createSavedStateHandle(),
                taskiFoxApplication().container.todosRepository,
                taskiFoxApplication().container.projectsRepository
            )
        }

        initializer {
            ProjectsViewModel(
                taskiFoxApplication().container.projectsRepository
            )
        }

        initializer {
            ProjectDetailsViewModel(
                this.createSavedStateHandle(),
                taskiFoxApplication().container.projectsRepository,
                taskiFoxApplication().container.todosRepository
            )
        }

        initializer {
            ModifyProjectViewModel(
                this.createSavedStateHandle(),
                taskiFoxApplication().container.projectsRepository
            )
        }

        initializer {
            SettingsViewModel(
                taskiFoxApplication().container.settingsRepository
            )
        }
    }
}

fun CreationExtras.taskiFoxApplication(): TaskiFoxApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskiFoxApplication)