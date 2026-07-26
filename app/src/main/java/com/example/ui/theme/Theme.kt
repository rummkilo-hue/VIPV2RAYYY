package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.domain.model.AppTheme

private val KhmerAngkorColorScheme = AngkorWatColorScheme

private val AmoledDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFE8DEF8),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    tertiary = Color(0xFFEFB8C8),
    background = AmoledBlack,
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = StatusRed
)

private val GlassDarkColorScheme = darkColorScheme(
    primary = Color(0xFF80D8FF),
    onPrimary = Color(0xFF00363A),
    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFF80CBC4),
    background = Color(0xFF0D1B2A),
    onBackground = Color(0xFFE0E1DD),
    surface = Color(0xFF1B263B),
    onSurface = Color(0xFFE0E1DD),
    surfaceVariant = Color(0xFF415A77),
    onSurfaceVariant = Color(0xFF778DA9),
    outline = Color(0xFF415A77),
    error = StatusRed
)

private val MidnightBlueColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = Color(0xFF90CAF9),
    background = Color(0xFF030A16),
    onBackground = Color(0xFFE1F5FE),
    surface = Color(0xFF0A192F),
    onSurface = Color(0xFFE1F5FE),
    surfaceVariant = Color(0xFF172A45),
    onSurfaceVariant = Color(0xFF8892B0),
    outline = Color(0xFF233554),
    error = StatusRed
)

@Composable
fun VIPV2RAYTheme(
    appTheme: AppTheme = AppTheme.KHMER_ANGKOR,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.KHMER_ANGKOR -> KhmerAngkorColorScheme
        AppTheme.AMOLED_DARK -> AmoledDarkColorScheme
        AppTheme.GLASS_DARK -> GlassDarkColorScheme
        AppTheme.MIDNIGHT_BLUE -> MidnightBlueColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
