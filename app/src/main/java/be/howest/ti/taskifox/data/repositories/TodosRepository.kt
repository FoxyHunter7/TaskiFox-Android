package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.models.ITodo
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TodosRepository {
    fun getAllTodosStream(): Flow<List<Todo>>
    fun getAllTodosWithProjectColStream(): Flow<List<TodoWithProjectColor>>
    fun getAllTodosGroupedByDueDateWithProjectColStream(): Flow<Map<LocalDate, List<TodoWithProjectColor>>>
    fun getTodo(id: Long): Flow<Todo>
    fun getAllTodosOfProject(projectId: Long): Flow<List<Todo>>
    suspend fun insert(todo: Todo)
    suspend fun update (todo: Todo)
    suspend fun delete (todo: ITodo)
}