package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VipDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF381E72),
    primaryContainer = ElectricPurple,
    onPrimaryContainer = Color(0xFFE8DEF8),
    secondary = PurpleGradientEnd,
    onSecondary = Color(0xFF332D41),
    tertiary = CyanGradientEnd,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    error = StatusRed
)

@Composable
fun VIPV2RAYTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VipDarkColorScheme,
        typography = Typography,
        content = content
    )
}
