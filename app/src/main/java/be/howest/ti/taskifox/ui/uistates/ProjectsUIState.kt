package be.howest.ti.taskifox.ui.uistates

import be.howest.ti.taskifox.models.Project

data class ProjectsUIState(
    val projects: List<Project> = listOf()
)