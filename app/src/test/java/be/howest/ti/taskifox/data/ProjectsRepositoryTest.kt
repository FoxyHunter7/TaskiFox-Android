package be.howest.ti.taskifox.data

import be.howest.ti.taskifox.data.repositories.RoomProjectsRepository
import be.howest.ti.taskifox.data.sources.room.ProjectDao
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class ProjectsRepositoryTest {
    private val project1 = Project(1, "t1", "d1", "#111111")
    private val project2 = Project(2, "t2", "d2", "#222222")
    private val project3 = Project(3, "t3", "d4", "#333333")

    @Mock
    lateinit var mockProjectDao: ProjectDao

    @InjectMocks
    lateinit var projectsRepository: RoomProjectsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        projectsRepository = RoomProjectsRepository(mockProjectDao)
    }

    @Test
    fun testGetAllProjectsStream() = runBlocking {
        `when`(mockProjectDao.getAllProjects()).thenReturn(getProjectListFlow())
        val result = projectsRepository.getAllProjectsStream().first()

        val expectedResult = listOf(project1, project2, project3)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetProject() = runBlocking {
        `when`(mockProjectDao.getProject(2)).thenReturn(flow { emit(project2) })
        val result = projectsRepository.getProject(2).first()

        assertEquals(project2, result)
    }

    private fun getProjectListFlow(): Flow<List<Project>> {
        return flow { emit(listOf(project1, project2, project3)) }
    }
}