package com.isradeleon.todoapp.data.repos

import androidx.lifecycle.LiveData
import com.isradeleon.todoapp.data.NoteDao
import com.isradeleon.todoapp.data.models.Note

class NoteRepo(
    private val noteDao: NoteDao
) {
    fun getAllData(): LiveData<List<Note>> = noteDao.getAllData()

    suspend fun getFilteredData(query: String) = noteDao.getFilteredData("%$query%")

    suspend fun getSortedByHigh() = noteDao.getSortedByHigh()

    suspend fun getSortedByLow() = noteDao.getSortedByLow()

    suspend fun insertData(note: Note) = noteDao.insertData(note)

    suspend fun updateData(note: Note) = noteDao.updateData(note)

    suspend fun deleteData(note: Note) = noteDao.deleteData(note)

    suspend fun deleteAll() = noteDao.deleteAll()
}