package be.howest.ti.taskifox.ui.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme

@Composable
fun Dialog(
    title: String,
    message: String,
    confirmBtnTxt: String,
    cancelBtnTxt: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onCancel()
            }) {
                Text(confirmBtnTxt)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(cancelBtnTxt)
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDialog() {
    TaskiFoxTheme {
        Dialog(
            title = "Confirmation",
            message = "Are you sure you want to delete this item?",
            confirmBtnTxt = "Confirm",
            cancelBtnTxt = "Cancel",
            onConfirm = { /* Handle confirmation */ },
            onCancel = { /* Handle dismissal */ }
        )
    }
}