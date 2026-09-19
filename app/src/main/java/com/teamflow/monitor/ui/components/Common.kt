package com.teamflow.monitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamflow.monitor.ui.theme.LocalAppColors

data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onLogout: () -> Unit,
    isDark: Boolean? = null,
    onToggleDark: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.primary)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
            } else {
                Icon(Icons.Default.Insights, contentDescription = null, tint = Color.White, modifier = Modifier.padding(start = 4.dp, end = 8.dp))
            }
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onToggleDark != null && isDark != null) {
                IconButton(onClick = onToggleDark) {
                    Icon(
                        if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Alternar tema",
                        tint = Color.White
                    )
                }
            }
            IconButton(onClick = onLogout) {
                Icon(Icons.Default.Logout, contentDescription = "Sair", tint = Color.White)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier, accentColor: Color? = null) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier
            .background(colors.card, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(label, color = colors.textMuted, fontSize = 12.sp)
        Spacer(Modifier.height(6.dp))
        Text(value, color = accentColor ?: colors.secondary, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    val colors = LocalAppColors.current
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            disabledContainerColor = colors.inactive
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun BottomNavBar(items: List<NavItem>, selected: String, onSelect: (String) -> Unit) {
    val colors = LocalAppColors.current
    NavigationBar(containerColor = colors.card) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.route == selected,
                onClick = { onSelect(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.secondary,
                    selectedTextColor = colors.secondary,
                    unselectedIconColor = colors.textMuted,
                    unselectedTextColor = colors.textMuted,
                    indicatorColor = colors.accentLight.copy(alpha = 0.5f)
                )
            )
        }
    }
}

/**
 * Simple horizontal bar chart built from plain Composables (no charting library needed).
 * Each entry is (label, value); bars are scaled against the max value in the list.
 */
@Composable
fun HorizontalBarChart(
    title: String,
    data: List<Pair<String, Int>>,
    barColor: Color? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val maxValue = (data.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.card, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.textPrimary)
        Spacer(Modifier.height(14.dp))
        if (data.isEmpty()) {
            Text("Sem dados suficientes ainda.", fontSize = 12.sp, color = colors.textMuted)
        }
        data.forEach { (label, value) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    label,
                    fontSize = 12.sp,
                    color = colors.textMuted,
                    modifier = Modifier.width(96.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(16.dp)
                        .background(colors.trackBg, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(value.toFloat() / maxValue.toFloat())
                            .background(barColor ?: colors.secondary, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    value.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.width(28.dp)
                )
            }
        }
    }
}
