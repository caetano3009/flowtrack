package com.teamflow.monitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.teamflow.monitor.AppViewModel
import com.teamflow.monitor.navigation.Routes
import com.teamflow.monitor.ui.theme.LocalAppColors

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val isDark by viewModel.darkMode.collectAsState()
    var isManager by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(colors.primary)) {
        IconButton(
            onClick = { viewModel.toggleDarkMode() },
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
        ) {
            Icon(
                if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Alternar tema",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))
            Icon(Icons.Default.Insights, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(8.dp))
            Text("FlowTrack", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Text("GESTÃO DE EQUIPES E PRODUÇÃO", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.background, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    "Acesse sua conta",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.textPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.card, RoundedCornerShape(10.dp))
                        .padding(4.dp)
                ) {
                    RoleTab("Gestor", isManager, Modifier.weight(1f)) { isManager = true }
                    RoleTab("Colaborador", !isManager, Modifier.weight(1f)) { isManager = false }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(if (isManager) "Usuário" else "Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (isManager) {
                            viewModel.loginManager(username.ifBlank { "Gestor" })
                            navController.navigate(Routes.MANAGER_DASHBOARD) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        } else {
                            viewModel.loginMember(username) {
                                navController.navigate(Routes.MEMBER_HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    if (isManager) "Gestor: qualquer usuário/senha (demo)" else "Colaborador: digite um nome já cadastrado (demo)",
                    fontSize = 11.sp,
                    color = colors.textMuted,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RoleTab(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .background(if (selected) colors.primary else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        Text(
            text,
            color = if (selected) Color.White else colors.textMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
