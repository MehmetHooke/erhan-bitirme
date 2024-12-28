package com.example.deneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

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



        return view
    }
}