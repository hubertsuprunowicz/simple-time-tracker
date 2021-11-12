package com.zk.timetracker.screens

import android.os.CountDownTimer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.zk.timetracker.ui.grey200
import com.zk.timetracker.ui.grey400
import com.zk.timetracker.ui.purple200
import com.zk.timetracker.ui.shapes
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

//@Preview
@Composable
fun TimeTrackerScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = grey200),

        ) {
        Header()
        Body(navController)
    }

}

@Composable
fun Header() {

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

            val timerState = remember { mutableStateOf(TextFieldValue("0")) }

//            val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())

            val timer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerState.value = TextFieldValue((millisUntilFinished / 1000).toString())
                }

                override fun onFinish() {
                    isWorking.value = false
                }
            }

            val onStop = {
                timer.cancel()
                timer.onTick(0)
                isWorking.value = false
            }

            val onStart = {
                timer.start()
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
                                    timerState.value.text,
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
                                    "34",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    color = Color.White
                                )
                                Text(
                                    " MINS",
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
                                    Text(text = "Billable")
                                    Switch(
                                        checked = isBillableState.value,
                                        onCheckedChange = { isBillableState.value = it }
                                    )
                                }


                                OutlinedButton(
                                    onClick = { if (isWorking.value) onStop() else onStart() },
                                    modifier = Modifier.size(55.dp),  //avoid the oval shape
                                    shape = CircleShape,
                                    border = BorderStroke(2.dp, Color.White),
                                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
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
                                    Text(text = "Remote")
                                    Switch(
                                        checked = isRemoteState.value,
                                        onCheckedChange = { isRemoteState.value = it }
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

@Composable
fun Body(navController: NavHostController) {
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
            LazyColumn {
                items(10) { index ->
                    Item(index, navController)
                }
            }
        }
    }
}

@Composable
fun Item(index: Int, navController: NavHostController) {
    Button(
        onClick = {
            navController.navigate("events/$index")
        },
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column() {
            Text("12:45 - 15:06", fontWeight = FontWeight.Bold)
            Row(
                Modifier
                    .padding(10.dp, 10.dp, 10.dp, 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Column() {
                    Text("Uber app design", fontWeight = FontWeight.Bold)
                    Text("Project X")
                }

                Column() {
                    Text("2 hrs 35 mins", fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            Icons.Filled.MonetizationOn,
                            contentDescription = "TODO",
                            Modifier.size(20.dp)
                        )
                        Icon(
                            Icons.Filled.SettingsRemote,
                            contentDescription = "TODO",
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