package com.teamflow.monitor.screens.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PersonAdd
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
fun ManagerMembersScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val isDark by viewModel.darkMode.collectAsState()
    val members by viewModel.teamMemberDao.getAll().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            AppTopBar(
                "Equipe",
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            BottomNavBar(
                items = managerNavItems,
                selected = Routes.MANAGER_MEMBERS,
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${members.size} colaboradores cadastrados", color = colors.textMuted, fontSize = 13.sp)
                Button(
                    onClick = { navController.navigate(Routes.MANAGER_ADD_MEMBER) },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Novo", fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(members) { m ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.card, RoundedCornerShape(10.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(m.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
                            Text(m.contact, color = colors.textMuted, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(m.schedule, color = colors.textMuted, fontSize = 12.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (m.active) colors.active.copy(alpha = 0.15f) else colors.inactive.copy(alpha = 0.2f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    if (m.active) "Ativo" else "Inativo",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (m.active) colors.active else colors.textMuted
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("${m.checkinCount} check-ins", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.secondary)
                        }
                    }
                }
            }
        }
    }
}
