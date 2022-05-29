package com.isradeleon.todoapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String
): Parcelable {
    fun compareTo(note: Note): Boolean{
        return note.let {
            this.id == it.id &&
            this.title == it.title &&
            this.priority == it.priority &&
            this.description == it.description
        }
    }
}