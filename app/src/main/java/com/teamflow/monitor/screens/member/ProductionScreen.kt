package com.teamflow.monitor.screens.member

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
fun ProductionScreen(navController: NavHostController, viewModel: AppViewModel) {
    val colors = LocalAppColors.current
    val session by viewModel.session.collectAsState()
    val isDark by viewModel.darkMode.collectAsState()
    val units = listOf("unidades", "kg", "horas", "litros", "m", "m²", "peças", "atendimentos", "Outro")

    var itemName by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf(units[0]) }
    var customUnit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    val dbDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    Scaffold(
        topBar = {
            AppTopBar(
                "Lançar Registro",
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
            if (!saved) {
                Text("Registro de Produção", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = colors.textPrimary)
                Text("Descreva o que foi feito ou produzido hoje.", color = colors.textMuted, fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))

                Text("O que foi feito / produzido? *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    placeholder = { Text("Ex: Peças montadas, Atendimentos, Relatórios...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(Modifier.height(16.dp))

                Text("Unidade *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
                Spacer(Modifier.height(6.dp))
                FlowChips(units, selectedUnit) { selectedUnit = it }
                if (selectedUnit == "Outro") {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customUnit,
                        onValueChange = { customUnit = it },
                        placeholder = { Text("Digite a unidade (ex: caixas, viagens...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))

                Text("Quantidade *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    placeholder = { Text("Ex: 12") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(Modifier.height(12.dp))

                Text("Observações", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.textPrimary)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Detalhes, condições, ocorrências...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(Modifier.height(20.dp))

                val finalUnit = if (selectedUnit == "Outro") customUnit.trim() else selectedUnit
                val qtyValid = quantity.replace(",", ".").toDoubleOrNull() != null
                val formValid = itemName.isNotBlank() && qtyValid && finalUnit.isNotBlank()

                Button(
                    onClick = {
                        val qty = quantity.replace(",", ".").toDoubleOrNull() ?: 0.0
                        viewModel.registerProduction(itemName.trim(), qty, finalUnit, dbDate, session.userName, notes.trim())
                        saved = true
                    },
                    enabled = formValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary, disabledContainerColor = colors.inactive)
                ) { Text("Salvar Registro", color = Color.White, fontWeight = FontWeight.Bold) }
            } else {
                val finalUnitVal = if (selectedUnit == "Outro") customUnit.trim() else selectedUnit
                Spacer(Modifier.height(30.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .background(colors.accentLight, RoundedCornerShape(50.dp))
                            .padding(20.dp)
                    ) { Icon(Icons.Default.Inventory2, contentDescription = null, tint = colors.primary, modifier = Modifier.size(32.dp)) }
                    Spacer(Modifier.height(16.dp))
                    Text("Registro Salvo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.textPrimary)
                    Text("Dados salvos com sucesso.", color = colors.textMuted, fontSize = 13.sp)
                    Spacer(Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.card, RoundedCornerShape(10.dp))
                            .padding(16.dp)
                    ) {
                        Text("RESUMO", color = colors.textMuted, fontSize = 11.sp)
                        Text("Item: $itemName", fontSize = 13.sp, color = colors.textPrimary)
                        Text("Quantidade: $quantity $finalUnitVal", fontSize = 13.sp, color = colors.textPrimary)
                        Text("Data: $dbDate", fontSize = 13.sp, color = colors.textPrimary)
                    }
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            saved = false
                            itemName = ""
                            quantity = ""
                            notes = ""
                            selectedUnit = units[0]
                            customUnit = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) { Text("Novo Registro", color = Color.White) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowChips(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    val colors = LocalAppColors.current
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { opt ->
            val isSel = opt == selected
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(if (isSel) colors.primary else colors.card, RoundedCornerShape(20.dp))
                    .clickable { onSelect(opt) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(opt, color = if (isSel) Color.White else colors.textPrimary, fontSize = 12.sp)
            }
        }
    }
}
