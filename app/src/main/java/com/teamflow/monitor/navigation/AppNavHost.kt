package com.teamflow.monitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teamflow.monitor.AppViewModel
import com.teamflow.monitor.screens.LoginScreen
import com.teamflow.monitor.screens.manager.AddMemberScreen
import com.teamflow.monitor.screens.manager.ManagerChartsScreen
import com.teamflow.monitor.screens.manager.ManagerDashboardScreen
import com.teamflow.monitor.screens.manager.ManagerHistoryScreen
import com.teamflow.monitor.screens.manager.ManagerMembersScreen
import com.teamflow.monitor.screens.member.CheckInScreen
import com.teamflow.monitor.screens.member.MemberHistoryScreen
import com.teamflow.monitor.screens.member.MemberHomeScreen
import com.teamflow.monitor.screens.member.ProductionScreen

object Routes {
    const val LOGIN = "login"
    const val MANAGER_DASHBOARD = "manager_dashboard"
    const val MANAGER_MEMBERS = "manager_members"
    const val MANAGER_ADD_MEMBER = "manager_add_member"
    const val MANAGER_CHARTS = "manager_charts"
    const val MANAGER_HISTORY = "manager_history"
    const val MEMBER_HOME = "member_home"
    const val MEMBER_CHECKIN = "member_checkin"
    const val MEMBER_PRODUCTION = "member_production"
    const val MEMBER_HISTORY = "member_history"
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) { LoginScreen(navController, viewModel) }

        composable(Routes.MANAGER_DASHBOARD) { ManagerDashboardScreen(navController, viewModel) }
        composable(Routes.MANAGER_MEMBERS) { ManagerMembersScreen(navController, viewModel) }
        composable(Routes.MANAGER_ADD_MEMBER) { AddMemberScreen(navController, viewModel) }
        composable(Routes.MANAGER_CHARTS) { ManagerChartsScreen(navController, viewModel) }
        composable(Routes.MANAGER_HISTORY) { ManagerHistoryScreen(navController, viewModel) }

        composable(Routes.MEMBER_HOME) { MemberHomeScreen(navController, viewModel) }
        composable(Routes.MEMBER_CHECKIN) { CheckInScreen(navController, viewModel) }
        composable(Routes.MEMBER_PRODUCTION) { ProductionScreen(navController, viewModel) }
        composable(Routes.MEMBER_HISTORY) { MemberHistoryScreen(navController, viewModel) }
    }
}
