package com.example.ui.screens.security

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PinLockScreen(
    correctPin: String,
    onUnlockSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    fun onKeyClick(digit: String) {
        if (enteredPin.length < 4) {
            enteredPin += digit
            isError = false
            if (enteredPin.length == 4) {
                if (enteredPin == correctPin) {
                    onUnlockSuccess()
                } else {
                    isError = true
                    enteredPin = ""
                }
            }
        }
    }

    fun onDeleteClick() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
            isError = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("VIPV2RAY SECURITY LOCK", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isError) "Incorrect PIN. Try again." else "Enter your 4-digit PIN",
                color = if (isError) StatusRed else TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // PIN Dots Indicator
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (i in 0 until 4) {
                    val filled = i < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(if (filled) NeonCyan else DarkSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Keypad
            val keys = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("", "0", "DEL")
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                keys.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { key ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .then(
                                        if (key.isNotEmpty()) {
                                            Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(DarkSurface)
                                                .clickable {
                                                    if (key == "DEL") onDeleteClick()
                                                    else onKeyClick(key)
                                                }
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (key == "DEL") {
                                    Icon(Icons.Default.Backspace, contentDescription = "Delete", tint = TextPrimary)
                                } else if (key.isNotEmpty()) {
                                    Text(key, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
