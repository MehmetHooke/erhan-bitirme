package com.example.deneme

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvCityName: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvWeatherDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        tvCityName = view.findViewById(R.id.tv_city_name)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvWeatherDescription = view.findViewById(R.id.tv_weather_description)

        fetchCurrentLocationWeather()
        return view
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocationWeather() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                fetchWeatherDataByCoordinates(it.latitude, it.longitude)
            }
        }
    }

    private fun fetchWeatherDataByCoordinates(latitude: Double, longitude: Double) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherApi::class.java)
        val call = api.getWeatherByCoordinates(latitude, longitude, "67b359b1a4aa4a91048279bffadb0853", "metric")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    tvCityName.text = "City: ${weatherResponse?.name}"
                    tvTemperature.text = "Temperature: ${weatherResponse?.main?.temp}°C"
                    tvWeatherDescription.text = weatherResponse?.weather?.firstOrNull()?.description?.let {
                        "Weather: $it"
                    } ?: "Weather information not available"
                } else {
                    Log.e("WeatherFragment", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherFragment", "API Call Failed: ${t.message}")
            }
        })
    }
}
