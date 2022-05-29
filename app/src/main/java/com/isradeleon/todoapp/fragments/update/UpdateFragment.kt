package com.isradeleon.todoapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isradeleon.todoapp.R
import com.isradeleon.todoapp.data.models.Priority
import com.isradeleon.todoapp.data.viewmodel.NoteViewModel
import com.isradeleon.todoapp.fragments.BaseActionFragment
import com.isradeleon.todoapp.data.viewmodel.SharedViewModel
import com.isradeleon.todoapp.databinding.FragmentUpdateBinding
import com.isradeleon.todoapp.utils.hideKeyboard

class UpdateFragment : BaseActionFragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    private val viewModel by viewModels<NoteViewModel>()
    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.note = args.currentItem
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
                is NoteViewModel.ActionState.DataDeleted -> onNoteSuccessfullyDeleted()
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menuSave -> updateNote()
            R.id.menuDelete -> deleteNoteConfirmation()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNoteConfirmation() {
        val note = args.currentItem
        AlertDialog.Builder(requireContext()).also {
            it.setTitle(R.string.delete)
            it.setMessage(R.string.delete_note_confirmation)
            it.setNegativeButton(R.string.cancel){ _, _ -> }
            it.setPositiveButton(R.string.ok){ _, _ ->
                viewModel.deleteNote(note)
            }
        }.create().show()
    }

    private fun updateNote() {
        val title = binding.layoutForm.titleEt.text.toString()
        val priority = binding.layoutForm.prioritySpinner.selectedItemPosition
        val description = binding.layoutForm.descriptionEt.text.toString()

        if (sharedViewModel.verifyNoteData(title, description)){
            // Proceed to update data
            val note = args.currentItem
            viewModel.updateNote(note.also {
                it.title = title
                it.priority = Priority.values()[priority]
                it.description = description
            })
        } else
            onFieldsEmpty()
    }

    override fun onFieldsSuccessfullySaved() {
        super.onFieldsSuccessfullySaved()
        activity?.onBackPressed()
    }

    private fun onNoteSuccessfullyDeleted(){
        hideKeyboard()
        Toast.makeText(requireContext(), R.string.data_deleted, Toast.LENGTH_SHORT).show()
        activity?.onBackPressed()
    }
}