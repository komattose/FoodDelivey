package com.example.myapplication6.utils

import kotlin.random.Random

class TemperatureMonitor {
    var currentTemperature: Float = 0.0f
        private set

    init {
        // Simulamos la lectura de la temperatura cada vez que se crea el monitor
        updateTemperature()
    }

    fun updateTemperature() {
        // Actualiza la temperatura de forma aleatoria entre -10 y 10 grados Celsius
        currentTemperature = Random.nextFloat() * 20 - 10
    }
}
