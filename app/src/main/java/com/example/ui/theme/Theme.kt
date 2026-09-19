package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DeepNavy,
    secondary = Purple,
    onSecondary = Color.White,
    secondaryContainer = SoftLavender,
    onSecondaryContainer = DeepNavy,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextMain,
    surface = CardWhite,
    onSurface = TextMain,
    surfaceVariant = SoftBlue,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Keep clean, light SaaS theme per requirements
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
