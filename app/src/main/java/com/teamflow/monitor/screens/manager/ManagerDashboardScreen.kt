package com.teamflow.monitor.screens.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.teamflow.monitor.AppViewModel
import com.teamflow.monitor.navigation.Routes
import com.teamflow.monitor.ui.components.AppTopBar
import com.teamflow.monitor.ui.components.BottomNavBar
import com.teamflow.monitor.ui.components.NavItem
import com.teamflow.monitor.ui.components.StatCard
import com.teamflow.monitor.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val managerNavItems = listOf(
    NavItem(Routes.MANAGER_DASHBOARD, "Painel", Icons.Default.Dashboard),
    NavItem(Routes.MANAGER_MEMBERS, "Equipe", Icons.Default.Group),
    NavItem(Routes.MANAGER_CHARTS, "Gráficos", Icons.Default.BarChart),
    NavItem(Routes.MANAGER_HISTORY, "Histórico", Icons.Default.History),
)

@Composable
fun ManagerDashboardScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val session by viewModel.session.collectAsState()
    val isDark by viewModel.darkMode.collectAsState()
    val members by viewModel.teamMemberDao.getAll().collectAsState(initial = emptyList())
    val records by viewModel.productionRecordDao.getAll().collectAsState(initial = emptyList())
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val todayCount by viewModel.checkInDao.getTodayCount(today).collectAsState(initial = 0)

    Scaffold(
        topBar = {
            AppTopBar(
                "FlowTrack",
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            BottomNavBar(
                items = managerNavItems,
                selected = Routes.MANAGER_DASHBOARD,
                onSelect = { navController.navigate(it) { launchSingleTop = true } }
            )
        },
        containerColor = colors.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Bom dia,", color = colors.textMuted, fontSize = 13.sp)
                Text(session.userName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Colaboradores Ativos", members.count { it.active }.toString(), Modifier.weight(1f))
                    StatCard("Registros no total", records.size.toString(), Modifier.weight(1f), accentColor = colors.gold)
                }
            }
            item {
                StatCard("Check-ins hoje", todayCount.toString(), Modifier.fillMaxWidth())
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WarningAmber, contentDescription = null, tint = colors.textPrimary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Alertas", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val inactiveCount = members.count { !it.active }
                    if (inactiveCount > 0) {
                        AlertRow("$inactiveCount colaborador(es) sem registro recente", true)
                    }
                    AlertRow("Entrega agendada para amanhã", false)
                    AlertRow("Relatório mensal disponível", false)
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Registros Recentes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
                    Text(
                        "Ver todos",
                        color = colors.secondary,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { navController.navigate(Routes.MANAGER_HISTORY) }
                    )
                }
            }
            items(records.take(3)) { r ->
                RecordRow(r.item, r.memberName, r.date, "${trimNum(r.quantity)} ${r.unit}")
            }
            item {
                Text("Ações Rápidas", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = { navController.navigate(Routes.MANAGER_ADD_MEMBER) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) { Text("Novo Colaborador", fontSize = 12.sp) }
                    Button(
                        onClick = { navController.navigate(Routes.MANAGER_HISTORY) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) { Text("Ver Histórico", fontSize = 12.sp) }
                }
            }
        }
    }
}

fun trimNum(d: Double): String {
    return if (d == d.toLong().toDouble()) d.toLong().toString() else d.toString()
}

@Composable
fun AlertRow(text: String, warning: Boolean) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (warning) colors.alert else colors.card, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Text(text, fontSize = 13.sp, color = if (warning) colors.alertText else colors.textPrimary)
    }
}

@Composable
fun RecordRow(item: String, who: String, date: String, amount: String) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.card, RoundedCornerShape(10.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(item, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.textPrimary)
            Text("$who · $date", color = colors.textMuted, fontSize = 12.sp)
        }
        Box(
            modifier = Modifier
                .background(colors.accentLight.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(amount, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        }
    }
}
