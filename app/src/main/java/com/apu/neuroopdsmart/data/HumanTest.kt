package com.apu.neuroopdsmart.data

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.apu.neuroopdsmart.R
import com.apu.neuroopdsmart.maketag
import com.apu.neuroopdsmart.now
import com.apu.neuroopdsmart.toInt
import java.time.Duration
import kotlinx.coroutines.delay

const val TAG = "TestSubsystem"

enum class HumanTestType(
    val id: Int,
    val title: String,
    val desc: String,
    val testContainer: HumanTest
) {
    BasicLightTest(
        100,
        "Базовый тест на свет",
        "Как только загорится лампочка, тыкните на экран.\nЗадача - сделать это как можно быстрее, но не раньше лампочки.", // ktlint-disable max-line-length
        BasicLightTest()
    ),
    BasicSoundTest(
        id = 101,
        title = "Тест на реакцию на звук",
        desc = "Когда раздастся сигнал, нажмите на кнопку. !!Не забудьте сделать звук погромче!!",
        BasicSoundTest()
    )
}

interface HumanTestInterface {
    val maxAttempts: Int

    var attempts: MutableState<Int>
    var results: MutableMap<Long, Float>

    val durationBeforeStart: Duration
    val durationTest: Duration

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

    override var attempts = mutableStateOf(0)
    override var results = mutableMapOf<Long, Float>()

    fun onAttempt(timeBegin: Long, timeSinceBegin: Long, result: Float) {
        attempts.value++
        results[timeSinceBegin] = result
        if (attempts.value >= this.maxAttempts) {
            stateOnSuccess()
        }
    }

    private fun stateOnSuccess() {
        onSuccess(results)
        Log.i(maketag(this), "stateOnSuccess: ${results.size}")
    }

    private fun stateOnFailed() {
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
) {
    @Composable
    override fun TestContainer() {
        Box(modifier = Modifier.fillMaxSize()) {
            var timeBegin by remember { mutableStateOf(-1L) }
            var timeSinceBegin by remember { mutableStateOf(-1L) }
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect("TestBeginning") {
                delay(durationBeforeStart.toMillis())
                timeBegin = System.currentTimeMillis()
                visible = true
                while (true) {
                    delay(10)
                    timeSinceBegin = now() - timeBegin
                }
            }
            IconButton(
                onClick = {
                    onAttempt(timeBegin, timeSinceBegin, timeSinceBegin / timeBegin.toFloat())
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

class BasicSoundTest : HumanTest(
    maxAttempts = 1,
    _dbeforeStart = 2000,
    _dTest = 10000
) {
    @Composable
    override fun TestContainer() {
        var timeBegin by remember { mutableStateOf(-1L) }
        var timeSinceBegin by remember { mutableStateOf(-1L) }
        val player = MediaPlayer.create(LocalContext.current, R.raw.quack_signal)
        LaunchedEffect("TestBeginning") {
            delay(durationBeforeStart.toMillis())
            player.start()
            timeBegin = System.currentTimeMillis()
            while (true) {
                delay(10)
                timeSinceBegin = now() - timeBegin
            }
        }
        Button(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick =
            {
                onAttempt(timeBegin, timeSinceBegin, timeSinceBegin / timeBegin.toFloat())
            },
            content =
            {
                Text("Clickable")
            },
            shape = RoundedCornerShape(50)
        )
    }
}
