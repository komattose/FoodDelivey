package com.example.myapplication6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuScreen()
        }
    }
}

@Composable
fun MenuScreen() {
    var totalAmount by remember { mutableIntStateOf(0) }
    var distance by remember { mutableIntStateOf(0) }
    var deliveryCost by remember { mutableIntStateOf(0) }
    var amountInput by remember { mutableStateOf("") }
    var distanceInput by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    // State for Snackbar Host
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) // Snackbar for showing messages
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Menú", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Monto Total") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = distanceInput,
                    onValueChange = { distanceInput = it },
                    label = { Text("Distancia (km)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Convierte los valores de los campos y calcula el costo
                    totalAmount = amountInput.toIntOrNull() ?: 0
                    distance = distanceInput.toIntOrNull() ?: 0
                    deliveryCost = calculateAndSaveDeliveryCost(totalAmount, distance) {
                        // Limpiar los campos después de guardar
                        amountInput = ""
                        distanceInput = ""
                        // Mostrar mensaje de éxito
                        showSnackbar = true
                    }
                }) {
                    Text("Calcular y Guardar Costo de Despacho")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Costo de Despacho: $deliveryCost")

                // Mostrar Snackbar si showSnackbar es verdadero
                if (showSnackbar) {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Datos de despacho guardados exitosamente")
                        showSnackbar = false // Resetea el estado después de mostrar el mensaje
                    }
                }
            }
        }
    )
}

// Actualiza la función para recibir un callback que se llama al finalizar el guardado
private fun calculateAndSaveDeliveryCost(amount: Int, distance: Int, onSaved: () -> Unit): Int {
    val cost = when {
        amount >= 50000 -> 0
        amount in 25000..49999 -> 150 * distance
        else -> 300 * distance
    }

    // Guardar los datos del despacho en Firebase
    saveDispatchData(amount, distance, cost, onSaved)

    return cost
}

// Modifica la función para incluir un callback al guardar los datos
private fun saveDispatchData(amount: Int, distance: Int, cost: Int, onSaved: () -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance().reference

    if (user != null) {
        val dispatchId = UUID.randomUUID().toString()

        val dispatchData = mapOf(
            "totalAmount" to amount,
            "distance" to distance,
            "deliveryCost" to cost,
            "timestamp" to System.currentTimeMillis()
        )

        // Guarda los datos y llama al callback `onSaved` si se guarda con éxito
        database.child("dispatches").child(user.uid).child(dispatchId).setValue(dispatchData)
            .addOnSuccessListener {
                onSaved() // Llama al callback para limpiar campos y mostrar mensaje
            }
            .addOnFailureListener { exception ->
                // Maneja el error al guardar los datos
                exception.printStackTrace()
            }
    }
}
