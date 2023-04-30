package com.ts.NoteIt

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ts.NoteIt.model.Note

class viewModel : ViewModel() {

    var allNotes: MutableList<Note> = mutableListOf()
    val noteReferenceList: MutableList<DocumentReference> = mutableListOf()
    val dataSetChanged: MutableLiveData<Boolean> = MutableLiveData(false)
    var selectednote: Note? = null
    val db = Firebase.firestore
    var userID: String = ""
    var userEmailorPhone = ""

    // val userDocRef = db.collection("users").document(userID).collection("notes")
    var existingUser = false


    fun createUserIfneeded() {
        if (!existingUser) {
            val data = hashMapOf("email" to userEmailorPhone)
            db.collection("users").document(userID).set(data)
                .addOnSuccessListener { Log.d("##addUserIfNeeded", "sucess") }
                .addOnFailureListener { e -> Log.w("##addUserIfNeeded", "failed=", e) }
            existingUser = true
        }
    }

    fun insert(title: String, desc: String, note: String, priority: Int, date: String) {
        val noteDocument = hashMapOf(
            "title" to title,
            "description" to desc,
            "note" to note,
            "priority" to priority,
            "date" to date
        )

        Log.d("userID_inserting", userID)
        db.collection("users").document(userID).collection("notes")
            .add(noteDocument)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "insert success tag",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )

                Log.d("data set changing in viewModel", "true")
            }
            .addOnFailureListener { e ->
                Log.w("insert failure tag", "Error adding document", e)
            }

        // Add a new document with a generated ID
        /* db.collection("notes")
             .add(noteDocument)
             .addOnSuccessListener { documentReference ->
                 Log.d(
                     "insert success tag",
                     "DocumentSnapshot added with ID: ${documentReference.id}"
                 )

                 Log.d("data set changing in viewModel", "true")
             }
             .addOnFailureListener { e ->
                 Log.w("insert failure tag", "Error adding document", e)
             } */

    }


    fun checkinUserExistance() {
        Log.d("@@@userId within checking userexistance", userID)
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val docSnap = it.getResult()
                if (docSnap?.exists() == true) {
                    //document Exist
                    existingUser = true
                } else {
                    existingUser = false
                }
            } else
                Log.d("@@@failed", "failed")
        }


    }

    fun getall() {
        Log.d("userID_getall", userID)
        Log.d("runningstatus", "getall called")

        if (existingUser) {
            db.collection("users").document(userID).collection("notes")
                .get()
                .addOnSuccessListener { result ->
                    var i: Int = 0
                    for (document in result) {
                        //  allNotes.value!!.add(document.data)
                        val note = Note(
                            document.id,
                            document["title"].toString(),
                            document["description"].toString(),
                            document["note"].toString(),
                            document["priority"].toString().toInt(),
                            document["date"].toString(),
                            document.reference
                        )
                        allNotes.add(note)
                        //  allNotes.value?.get(i)?.get("title")?.let { Log.d("data", it.toString()) }
                        //i++
                    }
                    Log.d("runningstatus", "getall success listner called")
                    dataSetChanged.value = true
                }
                .addOnFailureListener { exception ->
                    Log.w("@@@error get all", "Error getting documents. notes is blank", exception)
                }
        }
    }


    suspend fun delete() {
        selectednote?.reference?.delete()

    }

    fun update(title: String, desc: String, note: String, priority: Int) {
        val noteDocument = hashMapOf(

            "title" to title,
            "description" to desc,
            "note" to note,
            "priority" to priority,
            "date" to selectednote?.date
        )
        Log.d("runningstatus", "update called")
        selectednote?.reference?.update(noteDocument as Map<String, Any>)
            ?.addOnSuccessListener {
                Log.d("updated", "updated successfully")
            }
        /*   db.collection("notes").whereEqualTo("id", noteDocument["id"]).get()
               .addOnSuccessListener {
                   it.forEach {
                       it.reference.set(noteDocument)
                   }
                   Log.d("runningstatus", "update succeess listner called")
                   db.collection("notes").whereEqualTo("id", noteDocument["id"]).get()
                       .addOnSuccessListener {
                           it.forEach { Log.d("updated value", it["title"].toString()) }
                           getall()
                       }

                   Log.d("runningstatus", "update succeess listner called")
                   getall()
               } */
    }

    fun getNoteUsingId(id: Int): MutableMap<String?, Any?> {
        var note: MutableMap<String?, Any?> = mutableMapOf()
        db.collection("users").document(userID).collection("notes").whereEqualTo("NoteID", id)
            .get()
            .addOnSuccessListener { result ->
                for (documents in result) {
                    note = documents.data
                    break
                }
            }
        return note
    }

    fun getFilteredNotes(priority: Int): MutableList<Note> {
        val notes: MutableList<Note> = mutableListOf()
        for (note in allNotes) {
            val value = note.priority
            Log.d("value in loop=", value.toString() + "priority=$priority")
            if (value.toString() == priority.toString()) {
                notes.add(note)
            }
        }
        return notes
    }

}
