package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.models.Project
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getAllProjectsStream(): Flow<List<Project>>
    fun getProject(id: Long): Flow<Project?>
    suspend fun insert(project: Project)
    suspend fun update (project: Project)
    suspend fun delete (project: Project)
}