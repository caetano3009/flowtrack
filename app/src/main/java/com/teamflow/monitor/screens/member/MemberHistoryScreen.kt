package com.teamflow.monitor.screens.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.teamflow.monitor.AppViewModel
import com.teamflow.monitor.navigation.Routes
import com.teamflow.monitor.screens.manager.RecordRow
import com.teamflow.monitor.screens.manager.trimNum
import com.teamflow.monitor.ui.components.AppTopBar
import com.teamflow.monitor.ui.components.BottomNavBar
import com.teamflow.monitor.ui.theme.LocalAppColors

@Composable
fun MemberHistoryScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val session by viewModel.session.collectAsState()
    val isDark by viewModel.darkMode.collectAsState()
    val records by viewModel.productionRecordDao.getForMember(session.userName).collectAsState(initial = emptyList())
    val checkins by viewModel.checkInDao.getForMember(session.userName).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            AppTopBar(
                "Meu Histórico",
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            BottomNavBar(
                items = memberNavItems,
                selected = Routes.MEMBER_HISTORY,
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
            Text("Presenças (${checkins.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
            Spacer(Modifier.height(8.dp))
            if (checkins.isEmpty()) {
                Text("Nenhuma presença registrada ainda.", color = colors.textMuted, fontSize = 13.sp)
            } else {
                checkins.take(5).forEach { a ->
                    Text("${a.date} — ${a.shift}", color = colors.textMuted, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Registros (${records.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(records) { r ->
                    RecordRow(r.item, r.memberName, r.date, "${trimNum(r.quantity)} ${r.unit}")
                }
            }
        }
    }
}
