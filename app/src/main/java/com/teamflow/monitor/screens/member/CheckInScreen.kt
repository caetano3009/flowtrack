package com.teamflow.monitor.screens.member

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.teamflow.monitor.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckInScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val session by viewModel.session.collectAsState()
    val isDark by viewModel.darkMode.collectAsState()
    var confirmed by remember { mutableStateOf(false) }
    var chosenShift by remember { mutableStateOf("") }
    val today = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }
    val dbDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    Scaffold(
        topBar = {
            AppTopBar(
                "Registrar Presença",
                onBack = { navController.popBackStack() },
                onLogout = { viewModel.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                isDark = isDark,
                onToggleDark = { viewModel.toggleDarkMode() }
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!confirmed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.card, RoundedCornerShape(10.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Data de hoje", color = colors.textMuted, fontSize = 12.sp)
                        Text(today, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.textPrimary)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Selecione o turno:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                listOf(
                    "Manhã" to "07h00 – 11h00",
                    "Tarde" to "13h00 – 17h00",
                    "Integral" to "07h00 – 17h00"
                ).forEach { (label, time) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(colors.card, RoundedCornerShape(10.dp))
                            .clickable {
                                chosenShift = label
                                viewModel.registerCheckIn(session.userName, dbDate, chosenShift)
                                confirmed = true
                            }
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.textPrimary)
                            Text(time, color = colors.textMuted, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                Spacer(Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .background(colors.accentLight, RoundedCornerShape(50.dp))
                        .padding(20.dp)
                ) { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = colors.primary, modifier = Modifier.size(36.dp)) }
                Spacer(Modifier.height(20.dp))
                Text("Presença Registrada", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.textPrimary)
                Text("Seu check-in foi salvo com sucesso.", color = colors.textMuted, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.card, RoundedCornerShape(10.dp))
                        .padding(16.dp)
                ) {
                    Text("RESUMO DO REGISTRO", color = colors.textMuted, fontSize = 11.sp)
                    Text("Colaborador(a): ${session.userName}", fontSize = 13.sp, color = colors.textPrimary)
                    Text("Turno: $chosenShift", fontSize = 13.sp, color = colors.textPrimary)
                    Text("Data: $today", fontSize = 13.sp, color = colors.textPrimary)
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) { Text("Concluir", color = Color.White) }
            }
        }
    }
}
