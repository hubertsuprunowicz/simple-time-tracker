import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zk.timetracker.ui.grey200
import com.zk.timetracker.ui.purple200
import com.zk.timetracker.ui.shapes
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun TimeTrackerScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = grey200),

        ) {
        Header()
        Body()
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
                        //
                    }
                }
            }
        }
    }
}

@Composable
fun Body() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column {
            Text("Today", fontWeight = FontWeight.Bold)

        }
    }
}