package com.example.deneme

data class WeatherResponse(
    val name: String,              // Şehir ismi
    val main: Main,                // Ana sıcaklık bilgileri
    val weather: List<WeatherDetail> // Hava durumu açıklamaları
)

data class Main(
    val temp: Double               // Sıcaklık
)

data class WeatherDetail(
    val description: String        // Açıklama
)
