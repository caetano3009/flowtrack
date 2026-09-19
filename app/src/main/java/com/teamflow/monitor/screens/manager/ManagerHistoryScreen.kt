package com.teamflow.monitor.screens.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.teamflow.monitor.ui.theme.LocalAppColors

@Composable
fun ManagerHistoryScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val isDark by viewModel.darkMode.collectAsState()
    val records by viewModel.productionRecordDao.getAll().collectAsState(initial = emptyList())
    var filter by remember { mutableStateOf("Todos") }
    val items = listOf("Todos") + records.map { it.item }.distinct().take(5)
    val filtered = if (filter == "Todos") records else records.filter { it.item == filter }
    val distinctMembers = filtered.map { it.memberName }.distinct().size

    Scaffold(
        topBar = {
            AppTopBar(
                "Histórico de Registros",
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            BottomNavBar(
                items = managerNavItems,
                selected = Routes.MANAGER_HISTORY,
                onSelect = { navController.navigate(it) { launchSingleTop = true } }
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.gold, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("TOTAL DE REGISTROS", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    Text("${filtered.size}", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("COLABORADORES", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    Text("$distinctMembers", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEach { p ->
                    val selected = p == filter
                    Box(
                        modifier = Modifier
                            .clickable { filter = p }
                            .background(if (selected) colors.primary else colors.card, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(p, color = if (selected) androidx.compose.ui.graphics.Color.White else colors.textPrimary, fontSize = 12.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtered) { r ->
                    RecordRow(r.item, r.memberName, r.date, "${trimNum(r.quantity)} ${r.unit}")
                }
            }
        }
    }
}
