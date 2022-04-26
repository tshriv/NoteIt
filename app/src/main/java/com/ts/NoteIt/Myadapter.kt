package com.ts.NoteIt

import NoteIt.R
import NoteIt.databinding.ItemViewBinding
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ts.NoteIt.UI.HomeFragmentDirections
import com.ts.NoteIt.model.Note

class Myadapter(
    val requireContext: Context,
    var allNotes: MutableList<Note>,
    val viewModel: viewModel,
    val navController: NavController
) :
    RecyclerView.Adapter<Myadapter.NotesviewHolder>() {

    lateinit var note: MutableMap<String?, Any?>
    fun updateList(noteList: MutableList<Note>) {
        allNotes = noteList
        notifyDataSetChanged()
    }

    class NotesviewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesviewHolder {
        val binding: ItemViewBinding = ItemViewBinding.inflate(LayoutInflater.from(requireContext))
        return NotesviewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesviewHolder, position: Int) {
        bind(holder, position)

    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun bind(holder: NotesviewHolder, position: Int) {
        holder.binding.apply {
            val note = allNotes[position]
            textTitle.text = note.title
            textDescription.text = note.description
            textDate.text = note.date
            //setting Priority
            when (note.priority.toString().toInt()) {
                1 -> priorityColor.setBackgroundResource(R.drawable.red_dot)

                2 -> priorityColor.setBackgroundResource(R.drawable.yellow_dot)

                3 -> priorityColor.setBackgroundResource(R.drawable.green_dot)

            }
            Log.d("allnotes size in viewmodel", viewModel.allNotes.size.toString())

            holder.binding.root.setOnClickListener {

                viewModel.selectednote = note

                Navigation.findNavController(it)
                    .navigate(HomeFragmentDirections.actionHomeFragmentToEditNoteFragment())

            }
        }

    }


}

