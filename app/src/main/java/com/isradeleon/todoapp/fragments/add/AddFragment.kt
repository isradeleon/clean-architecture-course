package com.isradeleon.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.isradeleon.todoapp.R
import com.isradeleon.todoapp.data.viewmodel.NoteViewModel
import com.isradeleon.todoapp.fragments.BaseActionFragment
import com.isradeleon.todoapp.data.viewmodel.SharedViewModel
import com.isradeleon.todoapp.databinding.FragmentAddBinding

class AddFragment : BaseActionFragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NoteViewModel>()
    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.layoutForm.prioritySpinner.onItemSelectedListener = sharedViewModel.listener
        setHasOptionsMenu(true)
        subscribeObservers()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.actionState.observe(viewLifecycleOwner){
            when (it){
                is NoteViewModel.ActionState.DataInsertedOrUpdated -> onFieldsSuccessfullySaved()
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menuAdd -> saveNewNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNewNote() {
        val title = binding.layoutForm.titleEt.text.toString()
        val priority = binding.layoutForm.prioritySpinner.selectedItemPosition
        val description = binding.layoutForm.descriptionEt.text.toString()

        if (sharedViewModel.verifyNoteData(title, description))
            // Proceed to insert data
            viewModel.insertNote(title, priority, description)
        else
            onFieldsEmpty()
    }

    override fun onFieldsSuccessfullySaved() {
        super.onFieldsSuccessfullySaved()
        activity?.onBackPressed()
    }
}