package be.howest.ti.taskifox.data.sources.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.howest.ti.taskifox.models.Todo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY dueDateTime ASC")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT todos.*, projects.color AS projectColor FROM todos LEFT JOIN projects ON todos.projectId = projects.id ORDER BY todos.dueDateTime ASC")
    fun getAllTodosWithProjectColor(): Flow<List<TodoWithProjectColor>>

    @Query("SELECT * from todos WHERE id = :id")
    fun getTodo(id: Long): Flow<Todo>

    @Query("SELECT * from todos where projectId = :projectId")
    fun getAllTodosOfProject(projectId: Long): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}