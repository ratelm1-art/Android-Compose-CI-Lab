
package com.example.amphibians

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amphibians.model.Amphibian
import com.example.amphibians.ui.screens.AmphibianDetailScreen
import com.example.amphibians.ui.screens.AmphibiansScreen
import com.example.amphibians.ui.screens.AmphibiansViewModel
import com.example.amphibians.ui.theme.AmphibiansTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmphibiansTheme {
                AmphibiansApp()
            }
        }
    }
}

@Composable
fun AmphibiansApp(
    amphibiansViewModel: AmphibiansViewModel = viewModel(
        factory = AmphibiansViewModel.Factory
    )
) {
    val uiState by amphibiansViewModel.uiState.collectAsStateWithLifecycle()

    var selectedAmphibian by remember {
        mutableStateOf<Amphibian?>(null)
    }

    BackHandler(enabled = selectedAmphibian != null) {
        selectedAmphibian = null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        if (selectedAmphibian == null) {

            AmphibiansScreen(
                uiState = uiState,
                onRetry = {
                    amphibiansViewModel.getAmphibians()
                },
                onAmphibianClick = { amphibian ->
                    selectedAmphibian = amphibian
                },
                modifier = Modifier.padding(innerPadding)
            )

        } else {

            AmphibianDetailScreen(
                amphibian = selectedAmphibian!!,
                onBackClick = {
                    selectedAmphibian = null
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
Random Test