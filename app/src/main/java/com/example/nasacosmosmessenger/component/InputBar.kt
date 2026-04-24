package com.example.nasacosmosmessenger.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBar(
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("發送訊息...") },
            modifier = Modifier.weight(1f)
        )

        // 📅 日期按鈕
        IconButton(onClick = { showDatePicker = true }) {
            Icon(Icons.Default.DateRange, contentDescription = null)
        }

        // ➤ 發送
        IconButton(onClick = {
            onSend(text)
            text = ""
        }) {
            Icon(Icons.Default.Send, contentDescription = null)
        }
    }

    // ===== 日期選擇器 =====
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                text = date   // 🔥 自動填入
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millis = datePickerState.selectedDateMillis
                if (millis != null) {
                    val date = millisToDate(millis)
                    onDateSelected(date)
                }
            }) {
                Text("確定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun millisToDate(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(millis))
}