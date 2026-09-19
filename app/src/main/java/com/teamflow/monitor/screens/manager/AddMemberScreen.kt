package com.teamflow.monitor.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.teamflow.monitor.ui.components.PrimaryButton
import com.teamflow.monitor.ui.theme.LocalAppColors

@Composable
fun AddMemberScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val isDark by viewModel.darkMode.collectAsState()
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(
                "Novo Colaborador",
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Nome completo *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: João da Silva") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Contato", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
            OutlinedTextField(
                value = contact, onValueChange = { contact = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Telefone, e-mail ou WhatsApp") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Dias / turno disponível", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
            OutlinedTextField(
                value = schedule, onValueChange = { schedule = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: Segunda, Quarta - Manhã") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Observações", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
            OutlinedTextField(
                value = notes, onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Função, habilidades, restrições...") },
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = "Salvar Colaborador",
                enabled = name.isNotBlank(),
                onClick = {
                    viewModel.addMember(name.trim(), contact.trim(), schedule.trim(), notes.trim())
                    navController.popBackStack()
                }
            )
        }
    }
}
