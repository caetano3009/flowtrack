package com.teamflow.monitor.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val primary: Color,
    val secondary: Color,
    val accentLight: Color,
    val background: Color,
    val card: Color,
    val trackBg: Color,
    val gold: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val alert: Color,
    val alertText: Color,
    val inactive: Color,
    val active: Color,
    val divider: Color,
    val isDark: Boolean
)

val LightColors = AppColors(
    primary = Color(0xFF1E3A5F),
    secondary = Color(0xFF2563EB),
    accentLight = Color(0xFFBFDBFE),
    background = Color(0xFFF4F5F7),
    card = Color(0xFFFFFFFF),
    trackBg = Color(0xFFE7E9EE),
    gold = Color(0xFFB45309),
    textPrimary = Color(0xFF1E293B),
    textMuted = Color(0xFF64748B),
    alert = Color(0xFFFDEBD3),
    alertText = Color(0xFF9C4B0E),
    inactive = Color(0xFF94A3B8),
    active = Color(0xFF15803D),
    divider = Color(0xFFE2E5EA),
    isDark = false
)

val DarkColors = AppColors(
    primary = Color(0xFF3B82F6),
    secondary = Color(0xFF60A5FA),
    accentLight = Color(0xFF1E3A5F),
    background = Color(0xFF0F172A),
    card = Color(0xFF1E293B),
    trackBg = Color(0xFF334155),
    gold = Color(0xFFD97706),
    textPrimary = Color(0xFFE2E8F0),
    textMuted = Color(0xFF94A3B8),
    alert = Color(0xFF3A2B12),
    alertText = Color(0xFFF0A93E),
    inactive = Color(0xFF64748B),
    active = Color(0xFF4ADE80),
    divider = Color(0xFF334155),
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { LightColors }
