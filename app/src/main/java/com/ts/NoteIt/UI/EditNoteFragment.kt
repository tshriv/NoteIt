package com.ts.NoteIt.UI

import NoteIt.R
import NoteIt.databinding.FragmentEditNoteBinding
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ts.NoteIt.viewModel
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {


    val viewModel: viewModel by activityViewModels()
    lateinit var binding: FragmentEditNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditNoteBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  Log.d("is null", "Edittext- selectedNote of note is not null value of title is ${viewModel.selectednote!!.get("title")}")

        //checking viewmodel working
        Log.d("edittext: allnotes size in viewmodel", viewModel.allNotes.size.toString())
        if (viewModel.selectednote == null) {
            Log.d("is null", "selectedNote of note is null")
        } else
            Log.d(
                "is null",
                "selectedNote of note is not null value of title is ${viewModel.selectednote!!.title}"
            )


        Log.d("lets Log", viewModel.selectednote?.title.toString())

        binding.editTextTitle.setText(viewModel.selectednote?.title.toString())
        binding.editTextDesc.setText(viewModel.selectednote?.description.toString())
        binding.editTextNote.setText(viewModel.selectednote?.note.toString())


        when (viewModel.selectednote?.priority.toString().toInt()) {
            1 -> binding.redDot.setImageResource(R.drawable.ic_done)

            2 -> binding.yellowDot.setImageResource(R.drawable.ic_done)

            3 -> binding.greenDot.setImageResource(R.drawable.ic_done)
        }

        var priority: Int = viewModel.selectednote?.priority.toString().toInt()
        binding.redDot.setOnClickListener {
            priority = 1
            binding.redDot.setImageResource(R.drawable.ic_done)
            binding.yellowDot.setImageResource(0)
            binding.greenDot.setImageResource(0)
        }

        binding.yellowDot.setOnClickListener {
            priority = 2
            binding.yellowDot.setImageResource(R.drawable.ic_done)
            binding.redDot.setImageResource(0)
            binding.greenDot.setImageResource(0)
        }

        binding.greenDot.setOnClickListener {
            priority = 3
            binding.greenDot.setImageResource(R.drawable.ic_done)
            binding.redDot.setImageResource(0)
            binding.yellowDot.setImageResource(0)
        }

        binding.buttonDone.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val desc = binding.editTextDesc.text.toString()
            val noteText = binding.editTextNote.text.toString()

            viewModel.viewModelScope.launch {
                viewModel.update(title, desc, noteText, priority)
            }
            Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.delete_option_menu) {
            val bottomSheet = BottomSheetDialog(requireContext(), R.style.mybottom_dialog)
            bottomSheet.setContentView(R.layout.delete_dialog)
            bottomSheet.show()
            val deleteYes = bottomSheet.findViewById<TextView>(R.id.yes)
            val deleteNo = bottomSheet.findViewById<TextView>(R.id.no)

            deleteYes?.setOnClickListener {
                viewModel.viewModelScope.launch {
                    viewModel.delete()

                    Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
                    bottomSheet.dismiss()
                }
            }

            deleteNo?.setOnClickListener { bottomSheet.dismiss() }

            //   DeleteDialogBinding.inflate(layoutInflater)

        }
        return super.onOptionsItemSelected(item)
    }
}