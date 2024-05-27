package be.howest.ti.taskifox.data

import be.howest.ti.taskifox.data.repositories.RoomTodosRepository
import be.howest.ti.taskifox.data.sources.room.TodoDao
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
import java.time.ZonedDateTime

class TodosRepositoryTest {
    private val todo1 = Todo(1, "t1", "d1", ZonedDateTime.now(), null)
    private val todo2 = Todo(2, "t2", "d2", ZonedDateTime.now(), null)
    private val todo3 = Todo(3, "t3", "d3", ZonedDateTime.now(), 1)
    private val todo4 = Todo(4, "t4", "d4", ZonedDateTime.now(), 2)

    private val todoWithPCol1 =
        TodoWithProjectColor(1, "t1", "d1",
            ZonedDateTime.parse("2024-05-18T09:45:11+00:00"), 1, "#FFE412")
    private val todoWithPCol2 =
        TodoWithProjectColor(2, "t2", "d2",
            ZonedDateTime.parse("2024-05-18T14:22:24+00:00"), 1, "#FFE412")
    private val todoWithPCol3 =
        TodoWithProjectColor(2, "t3", "d3",
            ZonedDateTime.parse("2024-05-20T00:00:00+00:00"), 2, "#475212")
    private val todoWithPCol4 =
        TodoWithProjectColor(2, "t3", "d3",
            ZonedDateTime.parse("2024-05-20T10:40:00+00:00"), 2, "#475212")

    @Mock
    lateinit var mockTodoDao: TodoDao

    @InjectMocks
    lateinit var todosRepository: RoomTodosRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        todosRepository = RoomTodosRepository(mockTodoDao)
    }

    @Test
    fun testGetAllTodosChronicallyStream() = runBlocking {
        `when`(mockTodoDao.getAllTodos()).thenReturn(getTodoListFlow())
        val result = todosRepository.getAllTodosStream().first()

        val expectedResult = listOf(todo1, todo2, todo3, todo4)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetAllTodosWithProjectColorStream() = runBlocking {
        `when`(mockTodoDao.getAllTodosWithProjectColor()).thenReturn(getTodoWithPColFlow())
        val result = todosRepository.getAllTodosWithProjectColStream().first()

        val expectedResult = listOf(todoWithPCol1, todoWithPCol2, todoWithPCol3, todoWithPCol4)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetAllTodosGroupedByDueDateWithProjectColStream() = runBlocking {
        `when`(mockTodoDao.getAllTodosWithProjectColor()).thenReturn(getTodoWithPColFlow())
        val result = todosRepository.getAllTodosGroupedByDueDateWithProjectColStream().first()

        val expectedResult = mapOf(
            LocalDate.of(2024,5, 18) to listOf(todoWithPCol1, todoWithPCol2),
            LocalDate.of(2024,5,20) to listOf(todoWithPCol3, todoWithPCol4)
        )
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetProject() = runBlocking {
        `when`(mockTodoDao.getTodo(3)).thenReturn(flow { emit(todo3) })
        val result = todosRepository.getTodo(3).first()

        assertEquals(todo3, result)
    }

    @Test
    fun testGetTodosOfProject() = runBlocking {
        `when`(mockTodoDao.getAllTodosOfProject(2)).thenReturn(flow { emit(listOf(todo4)) })
        val result = todosRepository.getAllTodosOfProject(2).first()

        assertEquals(listOf(todo4), result)
    }

    private fun getTodoListFlow(): Flow<List<Todo>> {
        return flow { emit(listOf(todo1, todo2, todo3, todo4)) }
    }

    private fun getTodoWithPColFlow(): Flow<List<TodoWithProjectColor>> {
        return flow { emit(listOf(todoWithPCol1, todoWithPCol2, todoWithPCol3, todoWithPCol4)) }
    }
}