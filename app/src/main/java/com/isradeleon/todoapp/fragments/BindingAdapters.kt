package com.isradeleon.todoapp.fragments

import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.isradeleon.todoapp.R
import com.isradeleon.todoapp.data.models.Note
import com.isradeleon.todoapp.data.models.Priority
import com.isradeleon.todoapp.fragments.list.ListFragmentDirections

class BindingAdapters {
    companion object{
        @JvmStatic
        @BindingAdapter("android:listToAddFragment")
        fun listToAddFragment(view: FloatingActionButton, navigate: Boolean){
            if (navigate)
                view.setOnClickListener {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
        }

        @JvmStatic
        @BindingAdapter("android:priorityColor")
        fun priorityColor(view: View, priority: Priority){
            view.backgroundTintList = ContextCompat.getColorStateList(
                view.context, when(priority){
                    Priority.HIGH -> R.color.red
                    Priority.MEDIUM -> R.color.yellow
                    Priority.LOW -> R.color.green
                }
            )
        }

        @JvmStatic
        @BindingAdapter("android:listToUpdateFragment")
        fun listToUpdateFragment(cardView: CardView, note: Note){
            cardView.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(note)
                it.findNavController().navigate(action)
            }
        }
    }
}