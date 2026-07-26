package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassCard(
    cornerRadius: Dp = 20.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = DarkSurface,
    elevation: Dp = 4.dp
): Modifier = this
    .shadow(elevation, RoundedCornerShape(cornerRadius), ambientColor = NeonCyan.copy(alpha = 0.08f))
    .clip(RoundedCornerShape(cornerRadius))
    .background(backgroundColor)
    .border(
        width = 1.dp,
        color = borderColor,
        shape = RoundedCornerShape(cornerRadius)
    )

fun Modifier.glowingGlassCard(
    cornerRadius: Dp = 24.dp,
    glowColor: Color = NeonCyan
): Modifier = this
    .shadow(10.dp, RoundedCornerShape(cornerRadius), spotColor = glowColor)
    .clip(RoundedCornerShape(cornerRadius))
    .background(DarkSurfaceVariant)
    .border(
        width = 1.dp,
        brush = Brush.horizontalGradient(
            colors = listOf(
                glowColor.copy(alpha = 0.8f),
                GlassBorder,
                glowColor.copy(alpha = 0.3f)
            )
        ),
        shape = RoundedCornerShape(cornerRadius)
    )
