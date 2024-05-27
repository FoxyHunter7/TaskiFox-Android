package be.howest.ti.taskifox.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.howest.ti.taskifox.ui.theme.TaskiFoxTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    dateTime: ZonedDateTime,
    allowedYearRange: IntRange = DatePickerDefaults.YearRange,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    onDateTimeChange: (ZonedDateTime) -> Unit,
    dateRequired: Boolean = false,
    timeRequired: Boolean = false
) {
    val datePickerState = rememberDatePickerState(
        dateTime.toInstant().toEpochMilli(),
        yearRange = allowedYearRange,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = selectableDates
    )
    val timePickerState = rememberTimePickerState(
        initialHour = dateTime.hour,
        initialMinute = dateTime.minute
    )
    val dateDialogOpen = remember { mutableStateOf(false) }
    val timeDialogOpen = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(value = dateTime.toLocalDate()) }
    val selectedTime = remember { mutableStateOf(value = dateTime.toLocalTime()) }

    fun onChange() {
        onDateTimeChange(
            ZonedDateTime.of(LocalDateTime.of(selectedDate.value, selectedTime.value), dateTime.zone)
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate.value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            label = { Text("Date") },
            supportingText = { if (dateRequired) { Text("*Required") } },
            onValueChange = {},
            readOnly = true,
            enabled = true,
            trailingIcon = { IconButton(onClick = { dateDialogOpen.value = true }) {
                Icon(Icons.Default.CalendarMonth, "Calendar")
            } },
            modifier = Modifier
                .onFocusChanged { if (it.isFocused) { dateDialogOpen.value = true } }
                .weight(1f)

        )
        OutlinedTextField(
            value = selectedTime.value.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
            label = { Text("Time") },
            supportingText = { if (timeRequired) { Text("*Required") } },
            onValueChange = {},
            readOnly = true,
            enabled = true,
            trailingIcon = { IconButton(onClick = { timeDialogOpen.value = true }) {
                Icon(Icons.Default.AccessTime, "Time")
            } },
            modifier = Modifier
                .onFocusChanged { if (it.isFocused) { timeDialogOpen.value = true } }
                .weight(1f)
        )
    }

    if (dateDialogOpen.value) {
        DatePickerDialog(
            onDismissRequest = { dateDialogOpen.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate.value = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            onChange()
                            dateDialogOpen.value = false
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { dateDialogOpen.value = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    if (timeDialogOpen.value) {
        Dialog(
            onDismissRequest = { timeDialogOpen.value = false }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(20.dp)
                    .width(IntrinsicSize.Min)
            ) {
                Column {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp, 20.dp, 20.dp)
                    ) {
                        Button(
                            onClick = { timeDialogOpen.value = false }
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                selectedTime.value = LocalTime.of(timePickerState.hour, timePickerState.minute)
                                onChange()
                                timeDialogOpen.value = false
                            },
                            modifier = Modifier.padding(20.dp,0.dp,0.dp,0.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DateTimePickerPreview() {
    TaskiFoxTheme {
        DateTimePicker(
            dateTime = ZonedDateTime.now(),
            allowedYearRange = Year.now().value..Year.now().value + 5,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val currentDate = LocalDate.now()
                    val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !selectedDate.isBefore(currentDate)
                }
            },
            onDateTimeChange = {}
        )
    }
}