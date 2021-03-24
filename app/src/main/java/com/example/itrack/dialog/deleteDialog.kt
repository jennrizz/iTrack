package com.example.itrack.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.itrack.R

class deleteDialog: AppCompatDialogFragment() {
    lateinit var email: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_logout,null)
        val dialogBuilder = activity!!.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setView(dialogView)
        }

        dialogView.findViewById<Button>(R.id.signout_yes).setOnClickListener {

        }
        dialogView.findViewById<Button>(R.id.signout_no).setOnClickListener {

        }

        return dialogBuilder.create()
    }

}

