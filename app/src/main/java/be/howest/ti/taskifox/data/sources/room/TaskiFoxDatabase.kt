package be.howest.ti.taskifox.data.sources.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.models.Todo

@Database(entities = [Todo::class, Project::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskiFoxDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var Instance: TaskiFoxDatabase? = null

        fun getDatabase(context: Context): TaskiFoxDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TaskiFoxDatabase::class.java, "taskifox_database")
                    .fallbackToDestructiveMigration()
                    .addTypeConverter(Converters())
                    .build()
                    .also { Instance = it }
            }
        }
    }
}