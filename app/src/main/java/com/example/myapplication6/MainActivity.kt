package com.example.myapplication6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.myapplication6.ui.theme.MyApplication6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication6Theme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, context = this) // Pasa el contexto
            }
        }
    }
}
