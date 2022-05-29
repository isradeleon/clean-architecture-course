package com.isradeleon.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.isradeleon.todoapp.R
import com.isradeleon.todoapp.data.models.Note
import com.isradeleon.todoapp.data.viewmodel.NoteViewModel
import com.isradeleon.todoapp.databinding.FragmentListBinding
import com.isradeleon.todoapp.fragments.list.adapters.NotesAdapter
import com.isradeleon.todoapp.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    // Generated binding class
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NoteViewModel>()
    private val adapter: NotesAdapter by lazy {
        NotesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupRecycler()
        subscribeObservers()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    private fun subscribeObservers() {
        viewModel.getAllData().observe(viewLifecycleOwner) {
            adapter.setData(it)
            showNoDataLayout()
        }

        viewModel.actionState.observe(viewLifecycleOwner) {
            when (it){
                is NoteViewModel.ActionState.DataFiltered -> {
                    adapter.setData(it.notes)
                    binding.recyclerView.scrollToPosition(0)
                }
                is NoteViewModel.ActionState.DataSorted -> {
                    adapter.setData(it.notes)
                    binding.recyclerView.scrollToPosition(0)
                }
                else -> {}
            }
        }
    }

    private fun showNoDataLayout() {
        val isEmpty = adapter.itemCount == 0
        binding.noDataLayout.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setupRecycler() {
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setupSwipeToDelete(it)
            it.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 150
            }
        }
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.getData()[viewHolder.adapterPosition]
                viewModel.deleteNote(deletedItem)
                restoreDeletedData(recyclerView, deletedItem)
            }
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: Note){
        Snackbar.make(
            view, R.string.data_deleted, Snackbar.LENGTH_LONG
        ).setAction(R.string.undo){
            viewModel.insertNote(deletedItem)
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_list_menu, menu)
        (menu.findItem(R.id.menuSearch).actionView as SearchView).let {
            it.isSubmitButtonEnabled = true
            it.setOnQueryTextListener(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuDeleteAll -> deleteAllNotesConfirmation()
            R.id.menuPriorityHigh -> viewModel.sortNotesByHigh()
            R.id.menuPriorityLow -> viewModel.sortNotesByLow()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotesConfirmation() {
        AlertDialog.Builder(requireContext()).also {
            it.setTitle(R.string.delete)
            it.setMessage(R.string.delete_everything_confirmation)
            it.setNegativeButton(R.string.cancel){ _, _ -> }
            it.setPositiveButton(R.string.ok){ _, _ ->
                viewModel.deleteAllNotes()
            }
        }.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null)
            searchNotes(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null)
            searchNotes(newText)
        return true
    }

    private fun searchNotes(query: String){
        viewModel.getFilteredData(query)
    }
}