package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.data.sources.room.TodoDao
import be.howest.ti.taskifox.models.ITodo
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoomTodosRepository(private val todoDao: TodoDao) : TodosRepository {
    override fun getAllTodosStream(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    override fun getAllTodosWithProjectColStream(): Flow<List<TodoWithProjectColor>> {
        return todoDao.getAllTodosWithProjectColor()
    }

    override fun getAllTodosGroupedByDueDateWithProjectColStream(): Flow<Map<LocalDate, List<TodoWithProjectColor>>> {
        return todoDao.getAllTodosWithProjectColor().map {
            todos -> todos.groupBy { it.dueDateTime.toLocalDate() }
        }
    }

    override fun getTodo(id: Long): Flow<Todo> {
        return todoDao.getTodo(id)
    }

    override fun getAllTodosOfProject(projectId: Long): Flow<List<Todo>> {
        return todoDao.getAllTodosOfProject(projectId)
    }

    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    override suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    override suspend fun delete(todo: ITodo) {
        if (todo is TodoWithProjectColor) {
            todoDao.delete(Todo(
                id = todo.id,
                title = todo.title,
                description = todo.description,
                dueDateTime = todo.dueDateTime,
                projectId = todo.projectId
            ))
        } else {
            todoDao.delete(todo as Todo)
        }
    }
}