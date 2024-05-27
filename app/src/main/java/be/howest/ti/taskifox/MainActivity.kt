package be.howest.ti.taskifox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskiFoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TaskiFoxApp()
                }
            }
        }
    }
}