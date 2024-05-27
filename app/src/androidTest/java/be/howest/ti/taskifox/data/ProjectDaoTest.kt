package be.howest.ti.taskifox.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.howest.ti.taskifox.data.sources.room.Converters
import be.howest.ti.taskifox.data.sources.room.ProjectDao
import be.howest.ti.taskifox.data.sources.room.TaskiFoxDatabase
import be.howest.ti.taskifox.models.Project
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ProjectDaoTest {
    private lateinit var projectDao: ProjectDao
    private lateinit var taskiFoxDatabase: TaskiFoxDatabase
    private val project1 = Project(
        1,
        "Project1",
        "A description",
        "#FFFA78"
    )
    private val project2 = Project(
        2,
        "Project2",
        "A description",
        "#00AEF1"
    )
    private val project1Updated = Project(
        1,
        "Project1Updated",
        "A different description",
        "#FFFA78"
    )
    private val project2Updated = Project(
        2,
        "Project2Updated",
        "A different description",
        "#00AEF1"
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        taskiFoxDatabase = Room.inMemoryDatabaseBuilder(context, TaskiFoxDatabase::class.java)
            .addTypeConverter(Converters())
            .allowMainThreadQueries()
            .build()
        projectDao = taskiFoxDatabase.projectDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskiFoxDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsProjectIntoDB() = runBlocking {
        projectDao.insert(project1)

        val allProjects = projectDao.getAllProjects().first()
        Assert.assertEquals(project1, allProjects[0])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetProjects_returnsAllProjectsFromDB() = runBlocking {
        projectDao.insert(project1)
        projectDao.insert(project2)

        val allTodos = projectDao.getAllProjects().first()
        Assert.assertEquals(project1, allTodos[0])
        Assert.assertEquals(project2, allTodos[1])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetProject_returnsProjectFromDB() = runBlocking {
        projectDao.insert(project1)

        val project = projectDao.getProject(1)
        Assert.assertEquals(project1, project.first())
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteProjects_deletesAllProjectsFromDB() = runBlocking {
        projectDao.insert(project1)
        projectDao.insert(project2)

        projectDao.delete(project2)
        projectDao.delete(project1)

        val allTodos = projectDao.getAllProjects().first()
        Assert.assertTrue(allTodos.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateProjects_updatesProjectsInDB() = runBlocking {
        projectDao.insert(project1)
        projectDao.insert(project2)

        projectDao.update(project1Updated)
        projectDao.update(project2Updated)

        val allTodos = projectDao.getAllProjects().first()
        Assert.assertEquals(project1Updated, allTodos[0])
        Assert.assertEquals(project2Updated, allTodos[1])
    }
}