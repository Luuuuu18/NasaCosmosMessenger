package com.example.nasacosmosmessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nasacosmosmessenger.navigation.MainScreen
import com.example.nasacosmosmessenger.ui.theme.NASACosmosMessengerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NASACosmosMessengerTheme {
                MainScreen()
            }
        }
    }
}