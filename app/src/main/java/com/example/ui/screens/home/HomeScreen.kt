package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.Server
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    connectionState: ConnectionState,
    currentServer: Server?,
    currentPingMs: Int,
    downloadKbps: Float,
    uploadKbps: Float,
    durationSeconds: Long,
    todayUploadBytes: Long,
    todayDownloadBytes: Long,
    favoriteServers: List<Server>,
    onToggleConnection: () -> Unit,
    onSelectServerClick: () -> Unit,
    onSelectServer: (Server) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Khmer Angkor Wat Heritage Banner
        AngkorWatBannerCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Subscription Badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glassCard(cornerRadius = 16.dp)
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AngkorGold.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("VIP PRO", color = AngkorGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("VIP Subscription Active", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Unlimited Traffic • Exp: 2027-12-31", color = TextSecondary, fontSize = 11.sp)
                }
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(StatusGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("ONLINE", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Animated Connect Button
        ConnectButton(
            connectionState = connectionState,
            onToggleConnection = onToggleConnection
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Selected Server Selector Card (Khmer Angkor Gold Card)
        currentServer?.let { server ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AngkorGold)
                    .clickable(onClick = onSelectServerClick)
                    .padding(18.dp)
                    .testTag("home_current_server_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2A1A0E))
                        ) {
                            Text(text = server.flagEmoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "SELECTED SERVER",
                                color = Color(0xFF2A1A0E).copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = server.serverName,
                                color = Color(0xFF2A1A0E),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${server.protocol} • ${server.city.ifEmpty { server.countryName }}",
                                color = Color(0xFF2A1A0E).copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF2A1A0E).copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${currentPingMs}ms",
                                color = Color(0xFF2A1A0E),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Switch Server",
                            tint = Color(0xFF2A1A0E),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Speed Cards Grid (Download / Upload / Ping / Time)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Download Speed Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassCard(cornerRadius = 20.dp)
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Download",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "DOWNLOAD",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatSpeed(downloadKbps),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Upload Speed Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassCard(cornerRadius = 20.dp)
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Upload",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "UPLOAD",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatSpeed(uploadKbps),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Usage & Duration Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Connection Duration
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassCard(cornerRadius = 20.dp)
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "DURATION",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDuration(durationSeconds),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Today Usage
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassCard(cornerRadius = 20.dp)
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "TRAFFIC USED",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatBytes(todayDownloadBytes + todayUploadBytes),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (favoriteServers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.favorite_server),
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.quick_connect),
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(favoriteServers) { fav ->
                    Box(
                        modifier = Modifier
                            .glassCard(cornerRadius = 14.dp)
                            .clickable { onSelectServer(fav) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(fav.flagEmoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(fav.countryName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Star, contentDescription = null, tint = StatusOrange, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // padding for bottom nav
    }
}

private fun formatSpeed(kbps: Float): String {
    return if (kbps >= 1024) {
        String.format("%.1f MB/s", kbps / 1024f)
    } else {
        String.format("%.0f KB/s", kbps)
    }
}

private fun formatBytes(bytes: Long): String {
    val mb = bytes / (1024 * 1024)
    return if (mb >= 1024) {
        String.format("%.2f GB", mb / 1024f)
    } else {
        "$mb MB"
    }
}

private fun formatDuration(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hrs, mins, secs)
}
