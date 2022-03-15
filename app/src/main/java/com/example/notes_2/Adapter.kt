package com.example.notes_2

import Users
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class Adapter(val mCtx: Context, val layoutResId: Int, val list: List<Users> )
    : ArrayAdapter<Users>(mCtx,layoutResId,list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textTitle = view.findViewById<TextView>(R.id.textTitle)
        val textNotes = view.findViewById<TextView>(R.id.textNotes)

        val textUpdate = view.findViewById<TextView>(R.id.textUpdate)
        val textDelete = view.findViewById<TextView>(R.id.textDelete)

        val user = list[position]

        textTitle.text = user.title
        textNotes.text = user.notes

        textUpdate.setOnClickListener {
            showUpdateDialog(user)
        }
        textDelete.setOnClickListener {
            Deleteinfo(user)
        }

        return view
    }

    private fun showUpdateDialog(user: Users) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update, null)

        val textTitle = view.findViewById<EditText>(R.id.inputTitle)
        val textNotes = view.findViewById<EditText>(R.id.inputNotes)

        textTitle.setText(user.title)
        textNotes.setText(user.notes)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")

            val title = textTitle.text.toString().trim()

            val notes = textNotes.text.toString().trim()

            if (title.isEmpty()) {
                textTitle.error = "Please enter the title"
                textTitle.requestFocus()
                return@setPositiveButton
            }

            if (notes.isEmpty()) {
                textNotes.error = "Please enter your notes"
                textNotes.requestFocus()
                return@setPositiveButton
            }

            val user = Users(user.id, title, notes)

            dbUsers.child(user.id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx, "Updated", Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }

    private fun Deleteinfo(user: Users) {
        val progressDialog = ProgressDialog(
            context,
            com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting...")
        progressDialog.show()
        val mydatabase = FirebaseDatabase.getInstance().getReference("USERS")
        mydatabase.child(user.id).removeValue()
        Toast.makeText(mCtx, "Deleted!", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, show::class.java)
        context.startActivity(intent)
    }
}