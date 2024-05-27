package be.howest.ti.taskifox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DueTimeDisplay(dueDateTime: ZonedDateTime, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .background(
                color = backgroundColor.darken(0.15f),
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(10.dp, 0.dp)
            .height(25.dp),
        Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.AccessTime, contentDescription = "Due by ${dueDateTime.hour}",
            Modifier.height(18.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                dueDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

private fun Color.darken(fraction: Float): Color {
    val red = (this.red * (1 - fraction)).coerceIn(0f, 1f)
    val green = (this.green * (1 - fraction)).coerceIn(0f, 1f)
    val blue = (this.blue * (1 - fraction)).coerceIn(0f, 1f)
    val alpha = this.alpha

    return Color(red, green, blue, alpha)
}

@Preview
@Composable
fun DueTimeDisplayPreview() {
    TaskiFoxTheme {
        DueTimeDisplay(dueDateTime = ZonedDateTime.now(), backgroundColor = Color.Cyan)
    }
}