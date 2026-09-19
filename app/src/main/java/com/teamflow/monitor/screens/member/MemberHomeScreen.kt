package com.teamflow.monitor.screens.member

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

val memberNavItems = listOf(
    NavItem(Routes.MEMBER_HOME, "Início", Icons.Default.Home),
    NavItem(Routes.MEMBER_CHECKIN, "Presença", Icons.Default.CheckCircle),
    NavItem(Routes.MEMBER_PRODUCTION, "Registro", Icons.Default.Assignment),
    NavItem(Routes.MEMBER_HISTORY, "Histórico", Icons.Default.History),
)

@Composable
fun MemberHomeScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val session by viewModel.session.collectAsState()
    val isDark by viewModel.darkMode.collectAsState()
    val checkins by viewModel.checkInDao.getForMember(session.userName).collectAsState(initial = emptyList())
    val records by viewModel.productionRecordDao.getForMember(session.userName).collectAsState(initial = emptyList())
    val points = checkins.size * 20 + records.size * 20

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
                items = memberNavItems,
                selected = Routes.MEMBER_HOME,
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
            Text("Bem-vindo(a) de volta,", color = colors.textMuted, fontSize = 13.sp)
            Text(session.userName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.primary, RoundedCornerShape(14.dp))
                    .padding(18.dp)
            ) {
                Text("PRÓXIMO TURNO", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                Text("Amanhã — 07h às 11h", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("Unidade principal", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(colors.secondary, RoundedCornerShape(8.dp))
                        .clickable { navController.navigate(Routes.MEMBER_CHECKIN) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text("Confirmar Presença", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("Presenças", checkins.size.toString(), Modifier.weight(1f))
                StatCard("Registros", records.size.toString(), Modifier.weight(1f), accentColor = colors.gold)
                StatCard("Pontos", points.toString(), Modifier.weight(1f))
            }
            Spacer(Modifier.height(20.dp))
            Text("O que deseja fazer?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
            Spacer(Modifier.height(10.dp))
            ActionRow(Icons.Default.CheckCircle, "Registrar minha presença", "Check-in digital no turno de hoje") {
                navController.navigate(Routes.MEMBER_CHECKIN)
            }
            Spacer(Modifier.height(10.dp))
            ActionRow(Icons.Default.Assignment, "Lançar registro de produção", "Item, quantidade e unidade") {
                navController.navigate(Routes.MEMBER_PRODUCTION)
            }
            Spacer(Modifier.height(10.dp))
            ActionRow(Icons.Default.History, "Meu histórico", "Veja suas participações") {
                navController.navigate(Routes.MEMBER_HISTORY)
            }
        }
    }
}

@Composable
fun ActionRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.card, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.padding(end = 12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.textPrimary)
            Text(subtitle, color = colors.textMuted, fontSize = 12.sp)
        }
    }
}
