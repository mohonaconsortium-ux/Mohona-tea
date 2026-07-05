package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainErpScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ErpViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: ErpViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainErpScreen(viewModel = viewModel)
      }
    }
  }
}
