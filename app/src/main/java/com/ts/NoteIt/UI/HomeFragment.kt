package com.ts.NoteIt.UI

import NoteIt.R
import NoteIt.databinding.FragmentHomeBinding
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.auth.AuthUI
import com.ts.NoteIt.MainActivity
import com.ts.NoteIt.Myadapter
import com.ts.NoteIt.SignInActivity
import com.ts.NoteIt.model.Note
import com.ts.NoteIt.viewModel


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: Myadapter


    val viewModel: viewModel by activityViewModels()

    //val viewModel: viewModel by activityViewModels { viewModelFactory((activity?.application as NotesApplication).database.dao()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        // viewModel.viewModelScope.launch {
        // viewModel.insert(Note(1, "ko", "idf", "hjdf", 2, "datdd"))
        viewModel.userID = MainActivity.userID
        viewModel.userEmailorPhone = MainActivity.userEmailorPhone
        viewModel.checkinUserExistance()
        viewModel.createUserIfneeded()
        viewModel.allNotes = mutableListOf()
        viewModel.getall()
        setupRecyclerView(viewModel.allNotes)
        /*viewModel.allNotes.observe(viewLifecycleOwner) {allnotes->
            adapter.updateList(allnotes)
           // adapter.updateList(allnotes)
        }*/
        viewModel.dataSetChanged.observe(viewLifecycleOwner) { isDataSetChanged ->
            Log.d("runningstatus", "datasetchange listner called")
            if (isDataSetChanged == true) {
                adapter.updateList(viewModel.allNotes)
                viewModel.dataSetChanged.value = false
            }
        }

        //checking list
        Log.d("size list", viewModel.allNotes.size.toString())
        //      }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addnoteButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddNoteFragment())

        }

        binding.filterHigh.setOnClickListener {
            getFilteredNotes(1)
        }
        binding.filterMedium.setOnClickListener {
            getFilteredNotes(2)
        }
        binding.filterLow.setOnClickListener {
            getFilteredNotes(3)
        }
        binding.filterAll.setOnClickListener {
            getAllNotes()
        }

    }

    fun getFilteredNotes(priority: Int) {
        val note = viewModel.getFilteredNotes(priority)
        adapter.updateList(note)
    }

    fun getAllNotes() {
        //  viewModel.viewModelScope.launch { mainList = viewModel.getall() }
        adapter.updateList(viewModel.allNotes)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_fragment_menu, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val logout = menu.findItem(R.id.logout)
        val mySearchView = item.actionView as SearchView

        mySearchView.queryHint = "Search Notes Here"
        mySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchedList: MutableList<Note> = mutableListOf()
                for (note in viewModel.allNotes) {
                    if (note.title.contains(newText.toString())) {
                        searchedList.add(note)
                    }
                }
                adapter.updateList(searchedList)
                return true
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val builder = AlertDialog.Builder(context!!).setMessage("Do You Want to Logout?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                AuthUI.getInstance().signOut(requireContext())
                    .addOnCompleteListener {
                        val intent = Intent(requireContext(), SignInActivity::class.java)
                        requireActivity().finishAffinity()
                        startActivity(intent)
                    }
                dialog.dismiss()
            }).setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog, i -> dialog.dismiss() })

        builder.create()
        builder.show()


    }

    fun setupRecyclerView(list: MutableList<Note>) {
        adapter = Myadapter(requireContext(), list, viewModel, findNavController())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}