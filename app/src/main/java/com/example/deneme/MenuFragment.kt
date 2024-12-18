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

        return view
    }
}
