package be.howest.ti.taskifox.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.howest.ti.taskifox.data.sources.room.Converters
import be.howest.ti.taskifox.data.sources.room.ProjectDao
import be.howest.ti.taskifox.data.sources.room.TaskiFoxDatabase
import be.howest.ti.taskifox.data.sources.room.TodoDao
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.ZonedDateTime
import kotlin.Throws

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    private lateinit var todoDao: TodoDao
    private lateinit var projectDao: ProjectDao
    private lateinit var taskiFoxDatabase: TaskiFoxDatabase
    private val todo1 = Todo(
        1,
        "write android tests",
        "with 90% test coverage",
        ZonedDateTime.parse("2024-05-20T10:00:00+00:00"),
        null)
    private val todo2 = Todo(
        2,
        "defend the project",
        "with sweat and blood for it shall be glorious",
        ZonedDateTime.parse("2024-05-25T13:30:00+00:00"),
        null
    )
    private val todo1Updated = Todo(
        1,
        "write android tests",
        "With sufficient logical testing",
        ZonedDateTime.parse("2024-05-20T10:00:00+00:00"),
        null)
    private val todo2Updated = Todo(
        2,
        "defend the project",
        "Maybe a passing score would be nice at best 😅",
        ZonedDateTime.parse("2024-05-25T13:30:00+00:00"),
        null
    )
    private val todoWithProject = Todo(
        3,
        "some todo",
        "I'm part of a project",
        ZonedDateTime.parse("2024-05-25T13:30:00+00:00"),
        1
    )
    private val todoWithProjectColor = TodoWithProjectColor(
        3,
        "some todo",
        "I'm part of a project",
        ZonedDateTime.parse("2024-05-25T13:30:00+00:00"),
        1,
        "#FFFA78"
    )
    private val project = Project(
        1,
        "A Project",
        "A description",
        "#FFFA78"
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        taskiFoxDatabase = Room.inMemoryDatabaseBuilder(context, TaskiFoxDatabase::class.java)
            .addTypeConverter(Converters())
            .allowMainThreadQueries()
            .build()
        todoDao = taskiFoxDatabase.todoDao()
        projectDao = taskiFoxDatabase.projectDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskiFoxDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsTodoIntoDB() = runBlocking {
        todoDao.insert(todo1)

        val allTodos = todoDao.getAllTodos().first()
        assertEquals(todo1, allTodos[0])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllTodos_returnsAllTodosFromDB() = runBlocking {
        todoDao.insert(todo2)
        todoDao.insert(todo1)

        val allTodos = todoDao.getAllTodos().first()
        assertEquals(todo1, allTodos[0])
        assertEquals(todo2, allTodos[1])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetTodo_returnsTodoFromDB() = runBlocking {
        todoDao.insert(todo1)

        val todo = todoDao.getTodo(1)
        assertEquals(todo1, todo.first())
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteTodos_deletesAllTodosFromDB() = runBlocking {
        todoDao.insert(todo1)
        todoDao.insert(todo2)

        todoDao.delete(todo1)
        todoDao.delete(todo2)

        val allTodos = todoDao.getAllTodos().first()
        assertTrue(allTodos.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateTodos_updatesTodosInDB() = runBlocking {
        todoDao.insert(todo1)
        todoDao.insert(todo2)

        todoDao.update(todo1Updated)
        todoDao.update(todo2Updated)

        val allTodos = todoDao.getAllTodos().first()
        assertEquals(todo1Updated, allTodos[0])
        assertEquals(todo2Updated, allTodos[1])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllTodosWithProjectColor_returnsAllTodosFromDBWithColor() = runBlocking {
        projectDao.insert(project)
        todoDao.insert(todoWithProject)

        val allTodos = todoDao.getAllTodosWithProjectColor().first()
        assertEquals(todoWithProjectColor, allTodos[0])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllTodosOfProject_returnsAllTodosOfAProjectFromDB() = runBlocking {
        projectDao.insert(project)
        todoDao.insert(todoWithProject)

        val allTodos = todoDao.getAllTodosOfProject(1).first()
        assertEquals(todoWithProject, allTodos[0])
    }
}