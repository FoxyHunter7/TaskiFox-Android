package be.howest.ti.taskifox.ui.uistates

import be.howest.ti.taskifox.models.Project

data class ModifyProjectUIState(
    val project: Project = Project(
        0, "", "", "#"),
    val isProjectValid: Boolean = false
)
