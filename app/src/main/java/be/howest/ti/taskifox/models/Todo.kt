package be.howest.ti.taskifox.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.ZonedDateTime

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Todo(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    override val title: String,
    override val description: String,
    override val dueDateTime: ZonedDateTime,
    override val projectId: Long?
) : ITodo

data class TodoWithProjectColor(
    override val id: Long,
    override val title: String,
    override val description: String,
    override val dueDateTime: ZonedDateTime,
    override val projectId: Long?,
    val projectColor: String?
): ITodo

interface ITodo {
    val id: Long
    val title: String
    val description: String
    val dueDateTime: ZonedDateTime
    val projectId: Long?
}