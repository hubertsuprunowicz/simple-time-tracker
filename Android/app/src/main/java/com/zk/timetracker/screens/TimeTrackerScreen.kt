package com.zk.timetracker.screens

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zk.timetracker.models.Event
import com.zk.timetracker.models.eventsList
import com.zk.timetracker.ui.grey200
import com.zk.timetracker.ui.grey400
import com.zk.timetracker.ui.purple200
import com.zk.timetracker.ui.shapes
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.truncate

//@Preview
@Composable
fun TimeTrackerScreen(navController: NavHostController) {
    val events = remember { mutableStateListOf<Event>() }
    events.swapList(eventsList)

    Column(
        Modifier
            .fillMaxSize()
            .background(color = grey200),

        ) {
        Header(events)
        Body(navController, events)
    }

}

@Composable
fun Header(events: SnapshotStateList<Event>) {

    Surface(
        elevation = 8.dp,
    ) {
        Box(
            Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(15.dp, 20.dp)
        ) {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            val currentDate = sdf.format(Date())

            val isBillableState = remember { mutableStateOf(true) }
            val isRemoteState = remember { mutableStateOf(true) }
            val isWorking = remember { mutableStateOf(false) }
            val startedState = remember { mutableStateOf(LocalDateTime.now(ZoneOffset.UTC)) }
            val passedSeconds = remember { mutableStateOf(0) }

            val now: LocalDateTime = LocalDateTime.now()
            val end: LocalDateTime = now.plusDays(1).withHour(0).withMinute(0).withSecond(0)

            val secondsToEndOfTheDay =
                end.toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC)

            val countDownTimer: CountDownTimer =
                object : CountDownTimer(secondsToEndOfTheDay * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (!isWorking.value) {
                            cancel()
                            onFinish()
                        } else {
                            passedSeconds.value += 1
                        }
                    }

                    override fun onFinish() {
                        isWorking.value = false
                        passedSeconds.value = 0
                        println(eventsList)
                        eventsList.add(
                            Event(
                                id = eventsList.size,
                                userId = 0,
                                projectId = 0,
                                description = "Code",
                                isBillable = isBillableState.value,
                                isRemote = isRemoteState.value,
                                tagsId = mutableListOf(),
                                started = startedState.value.toString(),
                                ended = startedState.value.withSecond(passedSeconds.value)
                                    .toString()
                            )
                        )
                        events.swapList(eventsList)
                    }
                }

            fun startTimer() {
                isWorking.value = true
                countDownTimer.start()
            }

            fun onStop() {
                countDownTimer.cancel()
                passedSeconds.value = 0
                isWorking.value = false
            }

            fun onStart() {
                startTimer()
                isWorking.value = true
            }

            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(currentDate, fontWeight = FontWeight.Bold)
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(shapes.medium)
                ) {
                    Box(
                        Modifier
                            .background(purple200)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                            ) {
                                Text(
                                    "%.0f".format(floor(passedSeconds.value / 3600.0)),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    color = Color.White
                                )
                                Text(
                                    " HRS ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    "%.0f".format(floor(passedSeconds.value / 60.0) % 60),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    color = Color.White
                                )
                                Text(
                                    " MINS ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    "%d".format(passedSeconds.value % 60),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    color = Color.White
                                )
                                Text(
                                    " SECS",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column() {
                                    Text(text = "Billable", color = Color.White)
                                    Switch(
                                        checked = isBillableState.value,
                                        enabled = !isWorking.value,
                                        onCheckedChange = { isBillableState.value = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White
                                        ),
                                    )
                                }


                                OutlinedButton(
                                    onClick = { if (isWorking.value) onStop() else onStart() },
                                    modifier = Modifier.size(55.dp),
                                    shape = CircleShape,
                                    border = BorderStroke(2.dp, Color.White),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White,
                                        backgroundColor = purple200
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isWorking.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "content description"
                                    )
                                }
                                Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                                    Text(text = "Remote", color = Color.White)
                                    Switch(
                                        checked = isRemoteState.value,
                                        enabled = !isWorking.value,
                                        onCheckedChange = { isRemoteState.value = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White
                                        ),
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}

@Composable
fun Body(navController: NavHostController, events: SnapshotStateList<Event>) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(
            Modifier
                .padding(0.dp, 0.dp, 0.dp, 40.dp)
        ) {
            Text("Today", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.size(15.dp))
            LazyColumn() {
                items(events.size) { index ->
                    Item(events.reversed()[index], navController)
                }
            }
        }
    }
}

@Composable
fun Item(event: Event, navController: NavHostController) {
    Button(
        onClick = {
            navController.navigate("events/${event.id}")
        },
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        val hm = DateTimeFormatter.ofPattern("HH:mm")
        val startedDate = LocalDateTime.parse(event.started, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val endedDate = LocalDateTime.parse(event.started, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        Column() {
            Text(
                "${hm.format(startedDate)} - ${hm.format(endedDate)} ",
                fontWeight = FontWeight.Bold
            )
            Row(
                Modifier
                    .padding(10.dp, 10.dp, 10.dp, 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Column() {
                    Text(event.description, fontWeight = FontWeight.Bold)
                    Text("Project X")
                }

                Column() {
                    Text("2 hrs 35 mins", fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (event.isBillable)
                            Icon(
                                Icons.Filled.MonetizationOn,
                                contentDescription = "Billable event",
                                Modifier.size(20.dp)
                            )
                        if (event.isRemote)
                            Icon(
                                Icons.Filled.SettingsRemote,
                                contentDescription = "Remote event",
                                Modifier.size(20.dp)
                            )
                    }
                }

            }
        }

    }
    Divider(
        color = grey400, thickness = 1.dp, modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    )
}