package com.ts.NoteIt.model

import com.google.firebase.firestore.DocumentReference

data class Note(
    val NoteID: String,
    val title: String,
    val description: String,
    val note: String,
    val priority: Int,
    val date: String,
    val reference: DocumentReference
)