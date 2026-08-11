package com.example.kumiengine.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextPrimary,
    secondary = SecondaryEmerald,
    onSecondary = TextPrimary,
    tertiary = AccentCyan,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = ErrorRed
)

@Composable
fun KumiEngineTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
