package com.example.kumiengine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.kumiengine.ui.screens.MainScreen
import com.example.kumiengine.ui.theme.KumiEngineTheme
import com.example.kumiengine.viewmodel.KumiViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: KumiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KumiEngineTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
