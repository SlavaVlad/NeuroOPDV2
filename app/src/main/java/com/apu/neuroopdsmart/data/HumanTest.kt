package com.apu.neuroopdsmart.data

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.apu.neuroopdsmart.roundTo
import com.apu.neuroopdsmart.toInt
import java.time.Duration
import kotlinx.coroutines.delay

const val TAG = "TestSubsystem"

interface HumanTestInterface {
    val maxAttempts: Int

    var attempts: MutableState<Int>
    var results: MutableMap<Long, Float>

    val durationBeforeStart: Duration
    val durationTest: Duration
}

open class HumanTest(

    val id: Int,
    val name: String,
    val desc: String,

    override val maxAttempts: Int = 1,

    _dbeforeStart: Long = 5L,
    _dTest: Long = 10L,

    var onSuccess: (results: Map<Long, Float>) -> Unit = { _ -> },
    var onFailed: () -> Unit = {},
    var onCanceled: () -> Unit = {},

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HelloDialog(onStart: () -> Unit) {
        var showDialog by remember { mutableStateOf(true) }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp)
                        )
                        Row(Modifier.padding(8.dp)) {
                            FilledTonalButton(onClick = {
                                showDialog = false
                                onStart()
                            }) {
                                Text("Start test")
                            }
                            FilledTonalButton(onClick = {
                                showDialog = false
                                onCanceled()
                            }) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TestContainer() {
        TopAppBar(
            title = { Text(text = name, modifier = Modifier.padding(8.dp)) },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            navigationIcon = {
                IconButton(onClick = {
                    onFailed()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back"
                    )
                }
            })

        var startTest by remember {
            mutableStateOf(false)
        }

        HelloDialog() {
            startTest = true
        }

        if (startTest) {
            Spacer(modifier = Modifier.padding(8.dp))
            TestBody()
        }

    }

    @Composable
    open fun TestBody() {}
}

class BasicLightTest : HumanTest(
    100,
    "Базовый тест на свет",
    "Как только загорится лампочка, тыкните на экран.\nЗадача - сделать это как можно быстрее, но не раньше лампочки.",
    maxAttempts = 1,
    _dbeforeStart = 3000,
    _dTest = 10000
) {
    @Composable
    override fun TestBody() {
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
                    timeSinceBegin = now() - timeBegin - durationBeforeStart.toMillis()
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
                        (visible
                            .toInt()
                            .toFloat()
                                )
                    )
            )
        }
    }
}

class BasicSoundTest : HumanTest(
    id = 101,
    name = "Тест на реакцию на звук",
    desc = "Когда раздастся сигнал, нажмите на кнопку. !!Не забудьте сделать звук погромче!!",
    maxAttempts = 1,
    _dbeforeStart = 2000,
    _dTest = 10000
) {
    @Composable
    override fun TestBody() {

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
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp),
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
