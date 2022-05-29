package com.isradeleon.todoapp.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.isradeleon.todoapp.R
import com.isradeleon.todoapp.utils.hideKeyboard

abstract class BaseActionFragment: Fragment() {
    protected open fun onFieldsSuccessfullySaved(){
        hideKeyboard()
        Toast.makeText(requireContext(), getString(R.string.data_saved_successfully), Toast.LENGTH_SHORT).show()
    }

    protected open fun onFieldsEmpty() {
        Toast.makeText(requireContext(), getString(R.string.some_fields_empty), Toast.LENGTH_SHORT).show()
    }
}