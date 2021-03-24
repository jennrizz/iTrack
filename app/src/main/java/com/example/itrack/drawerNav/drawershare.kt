package com.example.itrack.drawerNav

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.itrack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [drawershare.newInstance] factory method to
 * create an instance of this fragment.
 */
class drawershare : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.drawer_share, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        fstore = FirebaseFirestore.getInstance()

        var shareApp = activity!!.getApplicationInfo()
        var apkPath = shareApp.sourceDir

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("application/vns.adroid.package.archive")
        var link ="https://github.com/jennrizz/iTrack"
        intent.putExtra(Intent.EXTRA_SUBJECT, link)
        startActivity(Intent.createChooser(intent,"Share app using"))
    }
}