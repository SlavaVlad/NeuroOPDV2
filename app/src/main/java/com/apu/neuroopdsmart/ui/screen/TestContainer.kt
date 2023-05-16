package com.apu.neuroopdsmart.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.data.BasicSoundTest
import com.apu.neuroopdsmart.ui.widgets.NeuroAppBar

const val TAG = "TestContainer"

@Composable
fun TestContainer(nav: NavController, api: ApiService, id: Int? = 101) {
    NeuroAppBar(content = {
        Spacer(Modifier.width(40.dp))
        // Text("Test ${HumanTestType.values().filter { it.id == id }[0].name}", maxLines = 1)
        Spacer(Modifier.width(60.dp))
    }, onNavButtonClicked = {
        nav.popBackStack()
    })

    BasicSoundTest().apply {
        onSuccess = { results ->
            Log.d(TAG, "TestContainer: ${results.values.toList()[0]}")
        }
        onFailed = {
            Log.d(TAG, "TestContainer: fail")
        }
    }.TestContainer()
}
