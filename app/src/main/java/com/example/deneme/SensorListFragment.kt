package com.example.deneme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SensorListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sensorAdapter: SensorDataAdapter
    private val sensorList = mutableListOf<SensorData>()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sensorAdapter = SensorDataAdapter(sensorList)
        recyclerView.adapter = sensorAdapter

        database = FirebaseDatabase.getInstance("https://erhanb-c2e6d-default-rtdb.firebaseio.com").reference
        fetchDataFromFirebase()

        return view
    }

    private fun fetchDataFromFirebase() {
        val sensorRef = database.child("sensor_data")
        sensorRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sensorList.clear()
                for (dataSnapshot in snapshot.children) {
                    val sensorData = dataSnapshot.getValue(SensorData::class.java)
                    sensorData?.let { sensorList.add(it) }
                }
                sensorAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database error: ${error.message}")
            }
        })
    }


}
