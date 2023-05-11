package com.apu.neuroopdsmart.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.model.TestResult
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.data.HumanTestType
import com.apu.neuroopdsmart.ui.NavigationManager
import com.apu.neuroopdsmart.ui.widgets.NeuroAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun TestContainer(nav: NavController, api: ApiService, id: Int? = 0) {
    NeuroAppBar(content = {
        Spacer(Modifier.width(40.dp))
        Text("Test ${HumanTestType.values().filter { it.id == id }[0].name}", maxLines = 1)
        Spacer(Modifier.width(60.dp))
    }, onNavButtonClicked = {
        nav.popBackStack()
    })

    val results = mutableListOf<TestResult>()

//    BasicLightTest(_type = HumanTestType.BasicLightTest, Duration.ofSeconds(10)).TestAnimationBody(
//        onFailed = {
//            Log.e("Test $id", "TestContainer: failed")
//            nav.popBackStack()
//        },
//        onTriggered = { result, time ->
//            results.add(TestResult(id, 1, result, time))
//        },
//        onCompleted = {
//            api.uploadTestResult(1, results[0]) {
//                nav.popBackStack()
//                Log.d("Test $id", "TestContainer: success")
//            }
//        },
//    )
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_6")
@Composable
fun Preview() {
    NavigationManager(CoroutineScope(Dispatchers.Main)).Navigation()
        .apply {
            navigate("test")
        }
}
