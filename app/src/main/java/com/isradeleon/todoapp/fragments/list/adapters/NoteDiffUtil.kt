package com.isradeleon.todoapp.fragments.list.adapters

import androidx.recyclerview.widget.DiffUtil
import com.isradeleon.todoapp.data.models.Note

class NoteDiffUtil(
    private val oldNotes: List<Note>,
    private val newNotes: List<Note>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldNotes.size

    override fun getNewListSize(): Int = newNotes.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes[oldItemPosition].id == newNotes[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes[oldItemPosition].compareTo(newNotes[newItemPosition])
    }
}