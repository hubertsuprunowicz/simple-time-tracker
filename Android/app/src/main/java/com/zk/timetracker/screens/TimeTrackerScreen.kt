package com.zk.timetracker.screens

import android.content.res.Resources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zk.timetracker.Timer
import com.zk.timetracker.error
import com.zk.timetracker.models.Event
import com.zk.timetracker.models.eventsList
import com.zk.timetracker.models.projectsList
import com.zk.timetracker.ui.grey200
import com.zk.timetracker.ui.grey400
import com.zk.timetracker.ui.purple200
import com.zk.timetracker.ui.shapes
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.floor

@Composable
fun TimeTrackerScreen(
    counter: MutableState<Int>, isPlaying: MutableState<Boolean>, timer: Timer
) {
    val events = remember { mutableStateListOf<Event>() }
    events.swapList(eventsList)

    Column(
        Modifier
            .fillMaxSize()
            .background(color = grey200),

        ) {
        Header(events, counter, isPlaying, timer)
        Body(events)
    }
}

@Composable
fun Header(
    events: SnapshotStateList<Event>, passedSeconds:
    MutableState<Int>,
    isPlaying: MutableState<Boolean>, timer: Timer
) {

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
            val startedState = remember { mutableStateOf(LocalDateTime.now(ZoneOffset.UTC)) }

            val now: LocalDateTime = LocalDateTime.now()
            val end: LocalDateTime = now.plusDays(1).withHour(0).withMinute(0).withSecond(0)

            val secondsToEndOfTheDay =
                end.toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC)

            fun onStart() {
                timer.play()
            }

            fun onStop() {
                timer.stop {
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
                            ended = startedState.value.plusSeconds(passedSeconds.value.toLong())
                                .toString()
                        )
                    )
                    events.swapList(eventsList)
                }
            }


            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(currentDate, fontWeight = FontWeight.Bold)

                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(25.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "event settings"
                        )
                    }
                }

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
                                        enabled = !isPlaying.value,
                                        onCheckedChange = { isBillableState.value = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White
                                        ),
                                    )
                                }


                                OutlinedButton(
                                    onClick = { if (isPlaying.value) onStop() else onStart() },
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
                                        imageVector = if (isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "content description"
                                    )
                                }
                                Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                                    Text(text = "Remote", color = Color.White)
                                    Switch(
                                        checked = isRemoteState.value,
                                        enabled = !isPlaying.value,
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
fun Body(events: SnapshotStateList<Event>) {
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
            EventsList(events)
        }
    }
}

@Composable
fun EventsList(events: SnapshotStateList<Event>) {
    val reversedEvents = events.reversed()
    LazyColumn() {
        items(reversedEvents) {
            Item(it)
        }
    }
}

@Composable
fun Item(event: Event) {
    val openDialog = remember { mutableStateOf(false) }
    val project =
        projectsList.find { it.id == event.projectId } ?: throw Resources.NotFoundException()

    Button(
        onClick = {
            openDialog.value = true
//            navController.navigate("events/${event.id}")
        },
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        val hm = DateTimeFormatter.ofPattern("HH:mm")
        val startedDate = LocalDateTime.parse(event.started, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val endedDate = LocalDateTime.parse(event.ended, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val diffSeconds: Long = ChronoUnit.SECONDS.between(startedDate, endedDate)
        val minutes = (diffSeconds / 60) % 60
        val hours = (diffSeconds / 60) / 60

        Column() {
            Text(
                "${hm.format(startedDate)} - ${hm.format(endedDate)}",
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
                    Text(project.name)
                }

                Column() {
                    Text("$hours hrs $minutes mins", fontWeight = FontWeight.Bold)
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

    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            },
            content = { DialogForm(closeDialog = { openDialog.value = false }, event = event) }
        )
    }
}

@Composable
fun DialogForm(closeDialog: () -> Unit, event: Event) {
    val hasError =
        remember {
            mutableMapOf(
                Pair("started", false),
                Pair("ended", false),
                Pair("description", false)
            )
        }

    val isSubmitEnabled = hasError.toList().none { it.second }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")
    val startDateTime = LocalDateTime.parse(event.started)
    val endDateTime = LocalDateTime.parse(event.ended)

    val startState = remember { mutableStateOf(TextFieldValue(formatter.format(startDateTime))) }
    val endState = remember { mutableStateOf(TextFieldValue(formatter.format(endDateTime))) }
    val descriptionState = remember { mutableStateOf(TextFieldValue(event.description)) }
    val isBillableState = remember { mutableStateOf(event.isBillable) }
    val isRemoteState = remember { mutableStateOf(event.isRemote) }

    val onStartChange: (TextFieldValue) -> Unit = {
        startState.value = it

        try {
            formatter.parse(it.text)
            hasError["started"] = false
        } catch (e: DateTimeParseException) {
            hasError["started"] = true
        }
    }

    val onEndChange: (TextFieldValue) -> Unit = {
        endState.value = it

        try {
            formatter.parse(it.text)
            hasError["ended"] = false
        } catch (
            e: DateTimeParseException
        ) {
            hasError["ended"] = true
        }
    }

    val onDescriptionChange: (TextFieldValue) -> Unit = {
        descriptionState.value = it
        hasError["description"] = it.text.length > 120
    }

    val onSubmit: () -> Unit = {
        eventsList.find {
            it.id == event.id
        }?.let {
            it.description = descriptionState.value.text
            it.isBillable = isBillableState.value
            it.isRemote = isRemoteState.value
            it.tagsId = listOf()
//            it.started = startState.value.toString()
//            it.ended = endState.value.toString()
        }

        closeDialog()
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp)
            .clip(RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.TopStart,

        ) {

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text("Today", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(15.dp))

                    OutlinedTextField(
                        label = { Text("Started (dd-MM-yyyy HH:mm)") },
                        value = startState.value,
                        onValueChange = onStartChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.error(hasError["started"] == true)
                    )
                    OutlinedTextField(
                        label = { Text("Ended (dd-MM-yyyy HH:mm)") },
                        value = endState.value,
                        onValueChange = onEndChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.error(hasError["ended"] == true)
                    )
                    OutlinedTextField(
                        label = { Text("Description") },
                        value = descriptionState.value,
                        onValueChange = onDescriptionChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.error(hasError["description"] == true)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Billable")
                            Switch(
                                checked = isBillableState.value,
                                enabled = true,
                                onCheckedChange = { isBillableState.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black
                                ),
                            )
                        }
                        Column {
                            Text(text = "Remote")
                            Switch(
                                checked = isRemoteState.value,
                                enabled = true,
                                onCheckedChange = { isRemoteState.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black
                                ),
                            )
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(enabled = true, onClick = closeDialog) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(enabled = isSubmitEnabled, onClick = onSubmit) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}