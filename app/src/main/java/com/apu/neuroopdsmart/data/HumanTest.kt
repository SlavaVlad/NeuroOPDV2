package com.apu.neuroopdsmart.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.MutableLiveData
import com.apu.neuroopdsmart.R
import com.apu.neuroopdsmart.maketag
import com.apu.neuroopdsmart.toInt
import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG = "TestSubsystem"

enum class HumanTestType(
    val id: Int,
    val title: String,
    val desc: String,
    val testContainer: HumanTest
) {
    BasicLightTest(
        0,
        "Базовый тест на свет",
        "Как только загорится лампочка, тыкните на экран.\nЗадача - сделать это как можно быстрее, но не раньше лампочки.", // ktlint-disable max-line-length
        BasicLightTest()
    )
}

interface HumanTestInterface {
    val maxAttempts: Int

    var attempts: MutableState<Int>
    var results: SnapshotStateMap<Long, Float>

    val durationBeforeStart: Duration
    val durationTest: Duration

    var isTestBegin: MutableLiveData<Boolean>
    var timeBegin: Long?
    var timeSinceStart: Long?

    fun onTestBegin()

    suspend fun timeController() {
        while (isTestBegin.value == true) {
            delay(1)
            timeSinceStart = System.currentTimeMillis() - timeBegin!!
            if ((timeSinceStart ?: -1) > timeBegin!!) {
                stateOnFailed()
            }
        }
    }

    fun stateOnAttempt(result: Float)
    fun stateOnSuccess(result: Float)
    fun stateOnFailed()

    // about test container and test UI
    @Composable
    fun TestContainer()
}

open class HumanTest(

    override val maxAttempts: Int = 1,

    _dbeforeStart: Long = 5L,
    _dTest: Long = 10L,

    var onSuccess: (results: Map<Long, Float>) -> Unit = { _ -> },
    var onFailed: () -> Unit = {}

) : HumanTestInterface {
    override val durationBeforeStart: Duration = Duration.ofMillis(_dbeforeStart)
    override val durationTest: Duration = Duration.ofMillis(_dTest)

    override var timeBegin: Long? = null
    override var timeSinceStart: Long? = null

    override var isTestBegin: MutableLiveData<Boolean> = MutableLiveData()
    override var attempts = mutableStateOf(0)
    override var results = mutableStateMapOf<Long, Float>()

    open override fun onTestBegin() {
        Log.d(TAG, "onTestBegin: test begin")
        timeBegin = System.currentTimeMillis()
        isTestBegin.value = true
        CoroutineScope(Dispatchers.Default).launch {
            timeController()
        }
    }

    open override fun stateOnAttempt(result: Float) {
        attempts.value++
        if (attempts.value >= this.maxAttempts) {
            stateOnSuccess(result)
        } else {
            results[timeSinceStart ?: -1] to result
        }
    }

    override fun stateOnSuccess(result: Float) {
        onSuccess(results)
        Log.i(maketag(this), "stateOnSuccess: ")
    }

    override fun stateOnFailed() {
        onFailed()
        Log.i(maketag(this), "stateOnFailed: ")
    }

    @Composable
    override fun TestContainer() {
    }
}

class BasicLightTest : HumanTest(
    maxAttempts = 1,
    _dbeforeStart = 3000,
    _dTest = 10000
) { // fixme: с задержкой перед тестом триггерабл не появляется
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    override fun TestContainer() {
        Box(modifier = Modifier.fillMaxSize()) {
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(durationBeforeStart.toMillis())
                visible = true
            }
            IconButton(
                onClick = {
                    stateOnAttempt(1f)
                },
                content = {
                    Icon(
                        painterResource(R.drawable.ic_circle_shape),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Touch me!"
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        (
                            visible
                                .toInt()
                                .toFloat()
                            )
                    )
            )
        }
    }
}
