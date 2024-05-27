package be.howest.ti.taskifox.ui.uistates

import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo

data class ProjectDetailsUIState(
    val projectTodos: List<Todo> = listOf()
)

data class ProjectDetailsUIStateProject(
    val project: Project = Project(
        0, "", "", "")
)