package com.teamflow.monitor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun FlowTrackTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val appColors = if (darkTheme) DarkColors else LightColors

    val materialColors = if (darkTheme) {
        darkColorScheme(
            primary = appColors.primary,
            secondary = appColors.secondary,
            background = appColors.background,
            surface = appColors.card,
            onPrimary = Color.White,
            onBackground = appColors.textPrimary,
            onSurface = appColors.textPrimary
        )
    } else {
        lightColorScheme(
            primary = appColors.primary,
            secondary = appColors.secondary,
            background = appColors.background,
            surface = appColors.card,
            onPrimary = Color.White,
            onBackground = appColors.textPrimary,
            onSurface = appColors.textPrimary
        )
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = AppTypography,
            content = content
        )
    }
}
