package com.isradeleon.todoapp.fragments.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isradeleon.todoapp.data.models.Note
import com.isradeleon.todoapp.databinding.ItemNoteBinding

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // ViewHolder with data binding
    class NoteViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note){
            binding.note = note
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NoteViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return NoteViewHolder(
                    ItemNoteBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    private var dataList = emptyList<Note>()

    fun setData(data: List<Note>){
        val diffUtil = NoteDiffUtil(dataList, data)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        dataList = data
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun getData() = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder.from(parent)

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = dataList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}