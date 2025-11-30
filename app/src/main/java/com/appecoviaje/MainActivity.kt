package com.appecoviaje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.appecoviaje.theme.AppEcoViajeTheme
import com.appecoviaje.ui.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppEcoViajeTheme {
                NavGraph()
            }
        }
    }
}
