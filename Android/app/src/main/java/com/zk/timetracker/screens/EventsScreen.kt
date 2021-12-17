import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zk.timetracker.models.Event
import com.zk.timetracker.models.eventsList
import com.zk.timetracker.models.getEvents
import com.zk.timetracker.models.projectsList
import com.zk.timetracker.ui.grey200
import com.zk.timetracker.ui.grey400
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun EventsScreen() {
    Column(Modifier.fillMaxSize()) {
        Header()
        Body()
    }
}

@Composable
fun Header() {
    Surface(
        elevation = 8.dp,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = grey200)
                .padding(15.dp)
        ) {
            Text(
                "Events View",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun Body() {
    List()
}

@Composable
fun List() {
    val page = remember { mutableStateOf(0) }
    val events = eventsList.getEvents(page.value)
    val listState = rememberLazyListState()

    // TODO: Scroll pagination
//    val reversedEvents = events.reversed()
    LazyColumn(state = listState) {
        items(events) {
            Item(it)
        }
    }

}

@Composable
fun Item(event: Event) {
    val openDialog = remember { mutableStateOf(false) }
    val project =
        projectsList.find { it.id == event.projectId } ?: throw Resources.NotFoundException()


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
    Divider(
        color = grey400, thickness = 1.dp, modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    )
}