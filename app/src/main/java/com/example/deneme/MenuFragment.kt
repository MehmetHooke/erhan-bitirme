package com.example.deneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        //realtimedatabasede verileri temizler


        // btn_weather butonunu bul ve tıklama işlemini tanımla
        val btnWeather = view.findViewById<Button>(R.id.btn_weather)
        btnWeather.setOnClickListener {
            // WeatherFragment'a geçiş yap
            val weatherFragment = WeatherFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, weatherFragment)
            transaction.addToBackStack(null) // Geri tuşu ile geri dönmek için
            transaction.commit()
        }

        val btn_sensor = view.findViewById<Button>(R.id.btn_sensor)
        btn_sensor.setOnClickListener {

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SensorListFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }


        // btn_disease butonunu bul ve tıklama işlemini tanımla
        val btnDisease = view.findViewById<Button>(R.id.btn_disease)
        btnDisease.setOnClickListener {
            // DiseaseFragment'a geçiş yap
            val diseaseFragment = HastalikFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, diseaseFragment)
            transaction.addToBackStack(null) // Geri tuşu ile geri dönmek için
            transaction.commit()
        }

        val cikisYapButton = view.findViewById<Button>(R.id.cikisYap)
        cikisYapButton.setOnClickListener {
            clearSensorData()
            FirebaseAuth.getInstance().signOut() // Firebase'den çıkış yap

            // LoginFragment'e geçiş
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment) // fragmentContainer, fragmentlerin yer aldığı container ID'si
                .commit()
        }



        return view
    }

    private fun clearSensorData() {
        val database = FirebaseDatabase.getInstance("https://erhanb-c2e6d-default-rtdb.firebaseio.com").reference
        database.child("sensor_data").removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "Sensor data cleared successfully.")
                } else {
                    Log.e("Firebase", "Failed to clear sensor data: ${task.exception?.message}")
                }
            }
    }

}