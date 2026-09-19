package com.teamflow.monitor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.teamflow.monitor.data.AppDatabase
import com.teamflow.monitor.data.CheckIn
import com.teamflow.monitor.data.ProductionRecord
import com.teamflow.monitor.data.TeamMember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class UserRole { MANAGER, MEMBER }

data class SessionState(
    val role: UserRole? = null,
    val userName: String = ""
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val teamMemberDao = db.teamMemberDao()
    val checkInDao = db.checkInDao()
    val productionRecordDao = db.productionRecordDao()

    private val _session = MutableStateFlow(SessionState())
    val session: StateFlow<SessionState> = _session.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    fun toggleDarkMode() {
        _darkMode.value = !_darkMode.value
    }

    fun loginManager(username: String) {
        _session.value = SessionState(role = UserRole.MANAGER, userName = username.ifBlank { "Gestor" })
    }

    fun loginMember(username: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val typed = username.trim().ifBlank { "Marina" }
            val found = teamMemberDao.findByName("%$typed%")
            _session.value = SessionState(role = UserRole.MEMBER, userName = found?.name ?: typed)
            onDone()
        }
    }

    fun logout() {
        _session.value = SessionState()
    }

    fun addMember(name: String, contact: String, schedule: String, notes: String) {
        viewModelScope.launch {
            teamMemberDao.insert(TeamMember(name = name, contact = contact, schedule = schedule, notes = notes))
        }
    }

    fun registerCheckIn(memberName: String, date: String, shift: String) {
        viewModelScope.launch {
            checkInDao.insert(CheckIn(memberName = memberName, date = date, shift = shift))
        }
    }

    fun registerProduction(item: String, quantity: Double, unit: String, date: String, memberName: String, notes: String) {
        viewModelScope.launch {
            productionRecordDao.insert(ProductionRecord(item = item, quantity = quantity, unit = unit, date = date, memberName = memberName, notes = notes))
        }
    }
}
