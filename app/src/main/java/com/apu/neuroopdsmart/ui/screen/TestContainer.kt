package com.apu.neuroopdsmart.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.data.IntermediateColorTest
import com.apu.neuroopdsmart.roundTo
import com.apu.neuroopdsmart.ui.widgets.NeuroAppBar

const val TAG = "TestContainer"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestContainer(nav: NavController, api: ApiService, id: Int? = 101) {
    NeuroAppBar(content = {
        Spacer(Modifier.width(40.dp))
        // Text("Test ${HumanTestType.values().filter { it.id == id }[0].name}", maxLines = 1)
        Spacer(Modifier.width(60.dp))
    }, onNavButtonClicked = {
        nav.popBackStack()
    })

    var results by remember { mutableStateOf<Map<Long, Float>>(emptyMap()) }

    var isSuccess by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    IntermediateColorTest().apply {
        onSuccess = { testResults ->
            Log.d(TAG, "TestContainer: ${testResults.values.toList()[0]}")
            isSuccess = true
            results = testResults

            showDialog = true
        }
        onFailed = {
            Log.d(TAG, "TestContainer: fail")
            isSuccess = false

            showDialog = true
        }
    }.TestContainer()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                nav.popBackStack()
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isSuccess) {
                        Text(text = "Test Completed Successfully")
                    } else {
                        Text(text = "Test Failed")
                    }
                    results.forEach { result ->
                        Text(text = "${(result.key / 1000f).roundTo(2)} s. : ${result.value.roundTo(1)}%")
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    FilledTonalButton(onClick = {
                        showDialog = false
                        nav.popBackStack()
                    }) {
                        Text("Ok")
                    }
                }
            }
        }
    }

}
