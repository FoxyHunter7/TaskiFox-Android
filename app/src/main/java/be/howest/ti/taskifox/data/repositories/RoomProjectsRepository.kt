package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.data.sources.room.ProjectDao
import be.howest.ti.taskifox.models.Project
import kotlinx.coroutines.flow.Flow

class RoomProjectsRepository(private val projectDao: ProjectDao) : ProjectsRepository {
    override fun getAllProjectsStream(): Flow<List<Project>> {
        return projectDao.getAllProjects()
    }

    override fun getProject(id: Long): Flow<Project?> {
        return projectDao.getProject(id)
    }

    override suspend fun insert(project: Project) {
        projectDao.insert(project)
    }

    override suspend fun update(project: Project) {
        projectDao.update(project)
    }

    override suspend fun delete(project: Project) {
        projectDao.delete(project)
    }
}