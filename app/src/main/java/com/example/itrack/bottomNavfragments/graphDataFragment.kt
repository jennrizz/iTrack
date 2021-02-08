package com.example.itrack.bottomNavfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.itrack.MainActivity
import com.example.itrack.R
import com.example.itrack.adapters.ListOfGraphSelection
import com.example.itrack.adapters.graphGridView


class graphDataFragment : Fragment(R.layout.fragment_graph_data) {
    lateinit var selectionView : GridView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectionView = view.findViewById(R.id.dataGraphGridView)
        val adapater = graphGridView(requireActivity(), addDataGrid())
        selectionView.adapter = adapater

        selectionView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, l ->
                    val imgPos = parent.getItemAtPosition(position)
                    val pos1 = parent.getItemAtPosition(0)
                    val pos2 = parent.getItemAtPosition(1)
                    val pos3 = parent.getItemAtPosition(2)
                    if (imgPos == pos1){
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                    else if (imgPos == pos2){
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                    else if (imgPos == pos3){
                        startActivity( Intent(activity, MainActivity::class.java))
                    }
                    else true
                }
    }

    private fun addDataGrid(): ArrayList<ListOfGraphSelection>{
        var sList = ArrayList<ListOfGraphSelection>()
        sList.add(ListOfGraphSelection(R.drawable.graph_icon, "BBT and Custom Data"))
        sList.add(ListOfGraphSelection(R.drawable.mood_tracker, "Mood Tracker"))
        sList.add(ListOfGraphSelection(R.drawable.step_icon, "Pedometer"))
        return sList
    }
    private fun activitySelected(){

    }
}