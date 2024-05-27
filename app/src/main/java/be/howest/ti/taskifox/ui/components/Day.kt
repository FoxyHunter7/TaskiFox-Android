package be.howest.ti.taskifox.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.ti.taskifox.models.ITodo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import be.howest.ti.taskifox.models.Weather
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Locale

@Composable
fun Day(
    date: LocalDate,
    todos: List<ITodo>,
    weather: Weather?,
    onTodoDelete: (ITodo) -> Unit,
    onTodoEdit: (Long) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .height(80.dp)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                "${date.dayOfMonth}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Column (
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    "${date.dayOfWeek}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    date.month.toString().lowercase().capitalise(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (weather != null) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current).data(weather.icon)
                            .crossfade(true ).build(),
                        contentDescription = "Weather Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight()
                            .graphicsLayer(scaleX = 1.5f, scaleY = 1.5f)
                    )
                    Text(
                        weather.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(40.dp, 0.dp, 20.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            todos.forEach { todo ->
                Todo(todo = todo, onDelete = onTodoDelete, onEdit = onTodoEdit)
            }
        }
    }
}

// Apparently .capitalise is deprecated and the IDE suggests doing this
// I couldn't find anything better either so...
// I hate it xD, don't judge 'm kay, thanks
private fun String.capitalise(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

@Preview(showBackground = true)
@Composable
fun DayPreview() {
    TaskiFoxTheme {
        Day(
            date = LocalDate.now(),
            todos = listOf(
                TodoWithProjectColor(
                    1,
                    "todo nr1",
                    "we are number 1",
                    ZonedDateTime.now(),
                    1,
                    null
                ),
                TodoWithProjectColor(
                    2,
                    "Some other todo",
                    "This todo is other than the other todos. This in the sense that it has a very long description and when I say long I mean about this long.",
                    ZonedDateTime.now(),
                    2,
                    "#FFAA00"
                ),
                TodoWithProjectColor(
                    2,
                    "very important",
                    "tho I forgot what it was about",
                    ZonedDateTime.now(),
                    2,
                    "#FFAA00"
                ),
                TodoWithProjectColor(
                    4,
                    "todo nr4",
                    "v 4 Vandetta",
                    ZonedDateTime.now(),
                    4,
                    "#ABED45"
                )
            ),
            weather = Weather(
                description = "ClearSky",
                icon = "https://openweathermap.org/img/wn/01d@2x.png"
            ),
            onTodoDelete = {},
            onTodoEdit = {}
        )
    }
}