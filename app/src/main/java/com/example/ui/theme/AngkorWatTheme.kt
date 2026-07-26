package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Angkor Wat Heritage Color Palette: Deep Royal Blues & Metallic Golds
val MetallicGoldPrimary = Color(0xFFFFD700)         // Bright Imperial Metallic Gold (#FFD700)
val MetallicGoldOnPrimary = Color(0xFF1B1400)       // Deep Gold Dark Text/Icon
val MetallicGoldContainer = Color(0xFF3D2E0A)       // Rich Metallic Gold Container
val MetallicGoldOnContainer = Color(0xFFFFE87C)     // Light Gold Accent

val DeepRoyalBlueBackground = Color(0xFF080D1A)     // Deep Kingdom Night Sky
val DeepRoyalBlueSurface = Color(0xFF101B2B)        // Royal Blue Temple Surface
val DeepRoyalBlueSurfaceVariant = Color(0xFF1A2A40) // Royal Blue Surface Variant
val DeepRoyalBlueBorder = Color(0xFF2E4366)         // Metallic Blue Border Accent

val AngkorGoldSecondary = Color(0xFFC59B27)        // Metallic Antique Gold
val AngkorGoldOnSecondary = Color(0xFF1A1200)      // On Secondary Text
val AngkorLotusPinkTertiary = Color(0xFFE05286)    // Sacred Lotus Flower Accent

val AngkorWatColorScheme: ColorScheme = darkColorScheme(
    primary = MetallicGoldPrimary,
    onPrimary = MetallicGoldOnPrimary,
    primaryContainer = MetallicGoldContainer,
    onPrimaryContainer = MetallicGoldOnContainer,
    secondary = AngkorGoldSecondary,
    onSecondary = AngkorGoldOnSecondary,
    tertiary = AngkorLotusPinkTertiary,
    background = DeepRoyalBlueBackground,
    onBackground = TextPrimary,
    surface = DeepRoyalBlueSurface,
    onSurface = TextPrimary,
    surfaceVariant = DeepRoyalBlueSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DeepRoyalBlueBorder,
    error = StatusRed
)

/**
 * Angkor Wat Theme Material 3 Wrapper
 * Applies deep royal blue and metallic gold color palette with Angkor Wat branding.
 */
@Composable
fun AngkorWatTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AngkorWatColorScheme,
        typography = Typography,
        content = content
    )
}
