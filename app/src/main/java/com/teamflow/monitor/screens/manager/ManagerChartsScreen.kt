package com.teamflow.monitor.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.teamflow.monitor.AppViewModel
import com.teamflow.monitor.navigation.Routes
import com.teamflow.monitor.ui.components.AppTopBar
import com.teamflow.monitor.ui.components.BottomNavBar
import com.teamflow.monitor.ui.components.HorizontalBarChart
import com.teamflow.monitor.ui.components.StatCard
import com.teamflow.monitor.ui.theme.LocalAppColors

@Composable
fun ManagerChartsScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val isDark by viewModel.darkMode.collectAsState()
    val members by viewModel.teamMemberDao.getAll().collectAsState(initial = emptyList())
    val records by viewModel.productionRecordDao.getAll().collectAsState(initial = emptyList())
    val checkins by viewModel.checkInDao.getAll().collectAsState(initial = emptyList())

    // Group records by item (count of entries per item, top 6)
    val byItem = records
        .groupingBy { it.item }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(6)
        .map { it.key to it.value }

    // Group records by member (count of entries per member, top 6)
    val byMember = records
        .groupingBy { it.memberName }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(6)
        .map { it.key to it.value }

    // Check-ins per member (top 6)
    val checkinsByMember = checkins
        .groupingBy { it.memberName }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(6)
        .map { it.key to it.value }

    Scaffold(
        topBar = {
            AppTopBar(
                "Gráficos",
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            BottomNavBar(
                items = managerNavItems,
                selected = Routes.MANAGER_CHARTS,
                onSelect = { navController.navigate(it) { launchSingleTop = true } }
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Visão Geral", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = colors.textPrimary)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Colaboradores", members.size.toString(), Modifier.weight(1f))
                StatCard("Registros", records.size.toString(), Modifier.weight(1f), accentColor = colors.gold)
                StatCard("Check-ins", checkins.size.toString(), Modifier.weight(1f))
            }

            HorizontalBarChart(
                title = "Registros por item",
                data = byItem,
                barColor = colors.secondary
            )

            HorizontalBarChart(
                title = "Registros por colaborador",
                data = byMember,
                barColor = colors.gold
            )

            HorizontalBarChart(
                title = "Check-ins por colaborador",
                data = checkinsByMember,
                barColor = colors.active
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}
