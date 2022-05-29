package com.isradeleon.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.isradeleon.todoapp.data.models.Note

@Dao
interface NoteDao {
    @Query("select * from notes order by id asc")
    fun getAllData(): LiveData<List<Note>>

    @Query("select * from notes where title like :query")
    suspend fun getFilteredData(query: String): List<Note>

    @Query("select * from notes order by case "
            + "when priority like 'H%' then 1 "
            + "when priority like 'M%' then 2 "
            + "when priority like 'L%' then 3 end"
    )
    suspend fun getSortedByHigh(): List<Note>

    @Query("select * from notes order by case "
            + "when priority like 'H%' then 3 "
            + "when priority like 'M%' then 2 "
            + "when priority like 'L%' then 1 end"
    )
    suspend fun getSortedByLow(): List<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(note: Note)

    @Update
    suspend fun updateData(note: Note)

    @Delete
    suspend fun deleteData(note: Note)

    @Query("delete from notes")
    suspend fun deleteAll()
}