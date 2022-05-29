package com.isradeleon.todoapp.data

import android.content.Context
import androidx.room.*
import com.isradeleon.todoapp.data.models.Note
import com.isradeleon.todoapp.data.models.Priority

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(ToDoDatabase.Converter::class)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: ToDoDatabase? = null

        operator fun invoke(context: Context): ToDoDatabase {
            val tempInstance = instance
            if (tempInstance != null)
                return tempInstance

            return synchronized(this){
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_db"
                ).build().also {
                    instance = it
                }
                newInstance
            }
        }

        /*operator fun invoke(context: Context) = instance ?: synchronized(this){
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "todo_db"
            ).build().apply {
                instance = this
            }
        }*/
    }

    class Converter {
        @TypeConverter
        fun fromPriority(priority: Priority): String{
            return priority.name
        }

        @TypeConverter
        fun toPriority(priority: String): Priority {
            return Priority.valueOf(priority)
        }
    }
}