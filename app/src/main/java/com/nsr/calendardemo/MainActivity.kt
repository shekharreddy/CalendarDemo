package com.nsr.calendardemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarApp() {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = "Customizable Calendar", color = Color.White) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White
                )
            },
//            backgroundColor = MaterialTheme.colorScheme.primary,
//            contentColor = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarHeader(selectedDate) { newDate ->
            selectedDate = newDate
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add day titles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (day in listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(6) { weekIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(7) { dayIndex ->
                        val day = weekIndex * 7 + dayIndex
                        val currentDate = Calendar.getInstance()
                        currentDate.time = selectedDate.time
                        currentDate.set(Calendar.DAY_OF_MONTH, 1)
                        currentDate.add(Calendar.DAY_OF_MONTH, day - currentDate.get(Calendar.DAY_OF_WEEK) + 1)
                        val isCurrentMonth = currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                        CalendarDay(
                            day = currentDate.get(Calendar.DAY_OF_MONTH),
                            isCurrentMonth = isCurrentMonth,
                            isSelected = currentDate == selectedDate,
                            onDaySelected = {
                                selectedDate = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarHeader(selectedDate: Calendar, onDateChange: (Calendar) -> Unit) {
    val monthFormatter = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onDateChange.invoke(previousMonth(selectedDate)) }) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(
            text = monthFormatter.format(selectedDate.time),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = { onDateChange.invoke(nextMonth(selectedDate)) }) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    onDaySelected: (Calendar) -> Unit
) {
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .widthIn(min = 45.dp, max = 45.dp)
            .aspectRatio(1f)
            .clickable { onDaySelected(createCalendarForDay(day)) }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Customize the UI of each day here
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = day.toString(),
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
            )
            if(day == 31 || day == 8) {
                Text(
                    text = "Due",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            // Add additional elements or styles as needed
            // For example, you can add an icon or change the background color
            // based on the value of isCurrentMonth or isSelected
        }
    }
}

fun createCalendarForDay(day: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar
}

fun previousMonth(calendar: Calendar): Calendar {
    val previousMonth = calendar.clone() as Calendar
    previousMonth.add(Calendar.MONTH, -1)
    return previousMonth
}

fun nextMonth(calendar: Calendar): Calendar {
    val nextMonth = calendar.clone() as Calendar
    nextMonth.add(Calendar.MONTH, 1)
    return nextMonth
}