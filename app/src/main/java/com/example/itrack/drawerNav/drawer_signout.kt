package com.example.itrack.drawerNav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.itrack.CreateAcc
import com.example.itrack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [drawer_signout.newInstance] factory method to
 * create an instance of this fragment.
 */
class drawer_signout : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser

    lateinit var refUsers: DatabaseReference
    lateinit var fstore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_signout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        fstore = FirebaseFirestore.getInstance()

        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_logout,null)
        val dialogBuilder = activity!!.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val alertDialog = dialogBuilder.show()
        dialogView.findViewById<Button>(R.id.signout_yes).setOnClickListener {
            //userEmail = view.findViewById<EditText>(R.id.reEmail).text.toString()
            alertDialog.dismiss()
            auth.signOut()
            val intent = Intent(activity, CreateAcc::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        dialogView.findViewById<Button>(R.id.signout_no).setOnClickListener {
            alertDialog.dismiss()
        }
    }
    public fun  openDialog(){


    }
}