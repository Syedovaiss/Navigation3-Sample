package com.ovais.navigation3sample.core.app.presentation

import Navigation3SampleNavigator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ovais.navigation3sample.core.ui.theme.Navigation3SampleTheme

class Navigation3SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation3SampleTheme {
                Navigation3SampleNavigator()
            }
        }
    }
}