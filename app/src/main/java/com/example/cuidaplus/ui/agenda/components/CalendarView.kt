package com.example.cuidaplus.ui.agenda.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val primaryColor = Color(0xFF1E3A8A)
    val currentMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.dayOfWeek.value
    val startOffset = if (firstDayOfWeek == 7) 6 else firstDayOfWeek - 1
    val dayLabelsFormatted = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.titlecase() } + " ${currentMonth.year}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            dayLabelsFormatted.forEach { day ->
                Text(day, fontWeight = FontWeight.SemiBold, color = primaryColor, fontSize = 12.sp)
            }
        }

        Column {
            var dayCount = 1
            for (week in 0 until 6) {
                if (dayCount > daysInMonth && week > 0) break
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    for (dayInWeekIndex in 0..6) {
                        val cellIndex = week * 7 + dayInWeekIndex
                        val isDayValid = cellIndex >= startOffset && dayCount <= daysInMonth
                        if (isDayValid) {
                            val currentDate = currentMonth.withDayOfMonth(dayCount)
                            val isSelected = currentDate == selectedDate
                            val isToday = currentDate == LocalDate.now()
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) primaryColor else if (isToday) Color(0xFFE0E7FF) else Color.Transparent)
                                    .clickable { onDateSelected(currentDate) }
                            ) {
                                Text(
                                    text = dayCount.toString(),
                                    color = if (isSelected) Color.White else if (isToday) primaryColor else Color.Black,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            dayCount++
                        } else {
                            Spacer(modifier = Modifier.size(36.dp))
                        }
                    }
                }
                if (dayCount > daysInMonth) break
            }
        }
    }
}
