package com.apu.neuroopdsmart.data

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.MutableLiveData
import com.apu.neuroopdsmart.R
import com.apu.neuroopdsmart.maketag
import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class HumanTestType(
    val id: Int,
    val title: String,
    val desc: String,
    testContainer: HumanTest
) {
    BasicLightTest(
        0,
        "Базовый тест на свет",
        "Как только загорится лампочка, тыкните на экран.\nЗадача - сделать это как можно быстрее, но не раньше лампочки.",
        BasicLightTest()
    )
}

interface HumanTestInterface {
    val maxAttempts: Int

    var attempts: MutableState<Int>
    var results: SnapshotStateList<Float>

    val durationBeforeStart: Duration
    val durationTest: Duration

    var isTestBegin: MutableLiveData<Boolean>
    var timeBegin: Long?
    var timeSinceStart: Long?

    suspend fun beforeStart(delayMillis: Long)

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

    fun stateOnAttempt(attempts: Int, result: Float)
    fun stateOnSuccess(attempts: Int, result: Float)
    fun stateOnFailed()

    // about test container and test UI
    fun TestContainer(
        onTriggered: (result: Float, time: Long) -> Unit
    )
}

open class HumanTest(

    override val maxAttempts: Int = 1,

    _dbeforeStart: Long = 5L,
    _dTest: Long = 10L,

    val onSuccess: (attempts: Int, result: Float) -> Unit = { _, _ -> },
    val onFailed: () -> Unit = {}

) : HumanTestInterface {
    override val durationBeforeStart: Duration = Duration.ofMillis(_dbeforeStart)
    override val durationTest: Duration = Duration.ofMillis(_dTest)

    override var timeBegin: Long? = null
    override var timeSinceStart: Long? = null

    override var isTestBegin: MutableLiveData<Boolean> = MutableLiveData()
    override var attempts = mutableStateOf(0)
    override var results = mutableStateListOf<Float>()

    open override suspend fun beforeStart(delayMillis: Long) {
        delay(delayMillis)
        onTestBegin()
    }

    open override fun onTestBegin() {
        timeBegin = System.currentTimeMillis()
        isTestBegin.value
        CoroutineScope(Dispatchers.Default).launch {
            timeController()
        }
    }

    open override fun stateOnAttempt(maxAttempts: Int, result: Float) {
        attempts.value++
        if (attempts.value >= this.maxAttempts) {
            onFailed()
        } else {
            results.add(((timeSinceStart ?: -1).toInt()), result)
        }
    }

    override fun stateOnSuccess(attempts: Int, result: Float) {
        onSuccess(attempts, result)
        Log.i(maketag(this), "stateOnSuccess: ")
    }

    override fun stateOnFailed() {
        onFailed()

    }

    open override fun TestContainer(onTriggered: (result: Float, time: Long) -> Unit) {
        runBlocking {
            beforeStart(durationBeforeStart.toMillis())
        }
    }
}

class BasicLightTest : HumanTest() {
    override fun TestContainer(
        onTriggered: (result: Float, time: Long) -> Unit
    ) {
        super.TestContainer(onTriggered)
        Box(modifier = Modifier.fillMaxSize()) {
            if (isTestBegin.value == true) {
                Box(modifier = Modifier.fillMaxSize())
            }
            isTestBegin.observeForever { testBegin ->
                if (testBegin == true) {
                    Icon(
                        painterResource(R.drawable.ic_circle_shape),
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Touch me!"
                    )
                }
            }
        }
    }
}
