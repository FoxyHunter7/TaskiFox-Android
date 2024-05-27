package be.howest.ti.taskifox.data.sources.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.howest.ti.taskifox.models.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * from projects WHERE id = :id")
    fun getProject(id: Long): Flow<Project>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(project: Project)

    @Update
    suspend fun update(todo: Project)

    @Delete
    suspend fun delete(todo: Project)
}