package be.howest.ti.taskifox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import be.howest.ti.taskifox.models.Project
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme

@Composable
fun Project(
    project: Project,
    onProjectClick: (Long) -> Unit
) {
    val projectColor = Color(project.color.toColorInt())

    TaskiFoxTheme(darkTheme = projectColor.luminance() < 0.5f) {
        Card(
            onClick = { onProjectClick(project.id) }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(projectColor.copy(alpha = 0.8f))
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    project.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectPreview() {
    TaskiFoxTheme {
        Project(
            project = Project(
                0,
                "NMA",
                "Native Mobile Apps (Howest TI S4)",
                color = "#ffaa00"
            ),
            onProjectClick = {}
        )
    }
}