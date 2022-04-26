package com.ts.NoteIt

import NoteIt.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    companion object {
        var userID: String = ""
        var userEmailorPhone: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = intent.extras?.get("userID").toString()
        userEmailorPhone = intent.extras?.get("userEmailorPhone").toString()
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))

        Log.d("@@@uid_mainActivityExtra", intent.extras?.get("userID").toString())

        Log.d("@@@uid_mainActivityCompanion", userID)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()

    }
}