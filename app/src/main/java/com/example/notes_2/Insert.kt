package com.example.notes_2

import Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert.*

class Insert : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        ref = FirebaseDatabase.getInstance().getReference("USERS")

        btn_save.setOnClickListener {
            savedata()
            val intent = Intent (this, show::class.java)
            startActivity(intent)
        }
    }

    private fun savedata() {
        val title = inputTitle.text.toString()
        val notes = inputNotes.text.toString()

        val userId = ref.push().key.toString()
        val user = Users(userId,title,notes)

        ref.child(userId).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
            inputTitle.setText("")
            inputNotes.setText("")
        }
    }
}