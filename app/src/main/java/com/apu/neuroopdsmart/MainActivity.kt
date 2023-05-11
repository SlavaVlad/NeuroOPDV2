package com.apu.neuroopdsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.apu.neuroopdsmart.ui.NavigationManager
import com.apu.neuroopdsmart.ui.theme.NeuroOpdSmartTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeuroOpdSmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationManager(CoroutineScope(Dispatchers.IO)).Navigation()
                }
            }
        }
    }
}

//@Preview(
//    showBackground = true,
//    heightDp = 700,
//    widthDp = 300,
//    device = "id:pixel_6",
//    showSystemUi = true,
//    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
//)
//@Composable
//fun DefaultPreview() {
//    NeuroOpdSmartTheme {
//        NavigationManager(CoroutineScope(Dispatchers.IO)).Navigation()
//    }
//}
