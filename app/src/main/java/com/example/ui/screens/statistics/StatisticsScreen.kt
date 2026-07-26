package com.example.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.LiveSpeedGraph
import com.example.ui.theme.*

enum class StatPeriod { DAILY, WEEKLY, MONTHLY }

data class ConnectionHistoryItem(
    val serverName: String,
    val flagEmoji: String,
    val protocol: String,
    val durationText: String,
    val dataTransferredText: String,
    val timeAgoText: String
)

@Composable
fun StatisticsScreen(
    uploadPoints: List<Float>,
    downloadPoints: List<Float>,
    avgPingMs: Int,
    todayUploadBytes: Long,
    todayDownloadBytes: Long,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf(StatPeriod.DAILY) }

    val mockHistory = listOf(
        ConnectionHistoryItem("VIP Singapore 01", "🇸🇬", "VLESS", "02:15:40", "1.45 GB", "Today, 14:20"),
        ConnectionHistoryItem("VIP Tokyo 01", "🇯🇵", "VMess", "01:05:12", "850 MB", "Yesterday, 21:00"),
        ConnectionHistoryItem("VIP Bangkok 01", "🇹🇭", "Reality", "00:42:10", "320 MB", "23 Jul 2026"),
        ConnectionHistoryItem("VIP Hong Kong 01", "🇭🇰", "Trojan", "03:10:00", "2.10 GB", "22 Jul 2026"),
        ConnectionHistoryItem("VIP Los Angeles 01", "🇺🇸", "TUIC", "00:25:30", "180 MB", "21 Jul 2026")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.traffic_statistics),
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Period selector tabs
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StatPeriod.values().forEach { period ->
                        val isSelected = selectedPeriod == period
                        val label = when (period) {
                            StatPeriod.DAILY -> stringResource(R.string.daily)
                            StatPeriod.WEEKLY -> stringResource(R.string.weekly)
                            StatPeriod.MONTHLY -> stringResource(R.string.monthly)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan else Color.Transparent)
                                .clickable { selectedPeriod = period }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) ElectricPurple else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Live Speed Graph
        item {
            LiveSpeedGraph(
                uploadDataPoints = uploadPoints,
                downloadDataPoints = downloadPoints
            )
        }

        // Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .glassCard(cornerRadius = 16.dp)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(StatusGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(stringResource(R.string.avg_ping), color = TextSecondary, fontSize = 11.sp)
                            Text("${avgPingMs} ms", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .glassCard(cornerRadius = 16.dp)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ElectricPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = PurpleGradientEnd, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(stringResource(R.string.avg_speed), color = TextSecondary, fontSize = 11.sp)
                            Text("12.4 MB/s", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // History Section Title
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.connection_history),
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // History List
        items(mockHistory) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(cornerRadius = 14.dp)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.flagEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(item.serverName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("${item.protocol} • ${item.durationText}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(item.dataTransferredText, color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(item.timeAgoText, color = TextMuted, fontSize = 10.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
