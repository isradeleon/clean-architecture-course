package com.isradeleon.todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.isradeleon.todoapp.data.ToDoDatabase
import com.isradeleon.todoapp.data.models.Note
import com.isradeleon.todoapp.data.models.Priority
import com.isradeleon.todoapp.data.repos.NoteRepo
import com.isradeleon.todoapp.data.viewmodel.extensions.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(
    application: Application
): AndroidViewModel(application) {
    private val noteDao = ToDoDatabase(application).noteDao()
    private val noteRepo: NoteRepo = NoteRepo(noteDao)

    val actionState : LiveData<ActionState> get() = _actionState
    private val _actionState = SingleLiveEvent<ActionState>()

    fun getAllData() = noteRepo.getAllData()

    fun getFilteredData(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = noteRepo.getFilteredData(query)
            withContext(Dispatchers.Main) {
                _actionState.value = ActionState.DataFiltered(notes)
            }
        }
    }

    fun sortNotesByHigh() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = noteRepo.getSortedByHigh()
            withContext(Dispatchers.Main) {
                _actionState.value = ActionState.DataSorted(notes)
            }
        }
    }

    fun sortNotesByLow() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = noteRepo.getSortedByLow()
            withContext(Dispatchers.Main) {
                _actionState.value = ActionState.DataSorted(notes)
            }
        }
    }

    fun insertNote(title: String, priority: Int, description: String) = this.insertNote(
        Note(
            0, title, Priority.values()[priority], description
        )
    )

    fun insertNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.insertData(note)
            withContext(Dispatchers.Main){
                _actionState.value = ActionState.DataInsertedOrUpdated
            }
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.updateData(note)
            withContext(Dispatchers.Main){
                _actionState.value = ActionState.DataInsertedOrUpdated
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.deleteData(note)
            withContext(Dispatchers.Main){
                _actionState.value = ActionState.DataDeleted
            }
        }
    }

    fun deleteAllNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.deleteAll()
        }
    }

    sealed class ActionState{
        object DataInsertedOrUpdated: ActionState()
        object DataDeleted: ActionState()
        data class DataFiltered(val notes: List<Note>): ActionState()
        data class DataSorted(val notes: List<Note>): ActionState()
    }
}