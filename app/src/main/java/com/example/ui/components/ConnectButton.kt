package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, DISCONNECTING
}

@Composable
fun ConnectButton(
    connectionState: ConnectionState,
    onToggleConnection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (connectionState == ConnectionState.CONNECTED || connectionState == ConnectionState.CONNECTING) 1.08f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val isConnected = connectionState == ConnectionState.CONNECTED
    val isConnecting = connectionState == ConnectionState.CONNECTING || connectionState == ConnectionState.DISCONNECTING

    val statusDotColor = when (connectionState) {
        ConnectionState.CONNECTED -> StatusGreen
        ConnectionState.CONNECTING, ConnectionState.DISCONNECTING -> StatusOrange
        ConnectionState.DISCONNECTED -> StatusRed
    }

    val glowBrush = Brush.radialGradient(
        colors = listOf(
            NeonCyan.copy(alpha = if (isConnected) 0.3f else 0.12f),
            NeonCyan.copy(alpha = 0.03f),
            Color.Transparent
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Pill Badge at Top
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(DarkSurface)
                .border(1.dp, if (isConnected) NeonCyan else GlassBorder, CircleShape)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusDotColor)
                )
                Text(
                    text = when (connectionState) {
                        ConnectionState.CONNECTED -> "បានភ្ជាប់ (Connected)"
                        ConnectionState.CONNECTING -> "កំពុងភ្ជាប់ (Connecting...)"
                        ConnectionState.DISCONNECTING -> "កំពុងផ្តាច់ (Disconnecting...)"
                        ConnectionState.DISCONNECTED -> "មិនទាន់ភ្ជាប់ (Disconnected)"
                    },
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Connect Circular Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(210.dp)
                .scale(if (isConnected || isConnecting) pulseScale else 1f)
                .background(glowBrush, CircleShape)
                .testTag("connect_button_outer")
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(176.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .border(width = 10.dp, color = DarkSurface, shape = CircleShape)
                    .shadow(16.dp, CircleShape, spotColor = NeonCyan)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggleConnection
                    )
                    .testTag("connect_button")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) StatusGreen else NeonCyan)
                            .shadow(12.dp, CircleShape, spotColor = NeonCyan)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Power Connection Button",
                            tint = ElectricPurple,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = when (connectionState) {
                            ConnectionState.CONNECTED -> "CONNECTED"
                            ConnectionState.CONNECTING -> "CONNECTING"
                            ConnectionState.DISCONNECTING -> "STOPPING"
                            ConnectionState.DISCONNECTED -> "TAP TO CONNECT"
                        },
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }
    }
}
