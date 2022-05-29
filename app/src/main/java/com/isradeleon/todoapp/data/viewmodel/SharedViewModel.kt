package com.isradeleon.todoapp.data.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.isradeleon.todoapp.R

class SharedViewModel(
    application: Application
): AndroidViewModel(application) {

    val listener = object: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position){
                0 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.red))
                1 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.yellow))
                2 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.green))
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun verifyNoteData(title: String, description: String): Boolean {
        return title.isNotBlank() && description.isNotBlank()
    }
}