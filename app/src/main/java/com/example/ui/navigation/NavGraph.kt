package com.example.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomTab
import com.example.ui.components.VIPBottomNavigation
import com.example.ui.components.VIPTopBar
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.configs.ConfigsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.security.PinLockScreen
import com.example.ui.screens.servers.ServersScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.statistics.StatisticsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.viewmodel.MainViewModel

enum class AppDestination { SPLASH, AUTH, MAIN, SETTINGS, PIN_LOCK }

@Composable
fun NavGraph(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var currentDestination by remember { mutableStateOf(AppDestination.SPLASH) }
    var currentTab by remember { mutableStateOf(BottomTab.HOME) }
    var isPinUnlocked by remember { mutableStateOf(false) }

    val settings by viewModel.settingsState.collectAsStateWithLifecycle()
    val servers by viewModel.serversState.collectAsStateWithLifecycle()
    val configs by viewModel.configsState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUserState.collectAsStateWithLifecycle()

    val connectionState by viewModel.connectionState.collectAsStateWithLifecycle()
    val selectedServer by viewModel.selectedServer.collectAsStateWithLifecycle()
    val activeConfig by viewModel.activeConfig.collectAsStateWithLifecycle()

    val downloadKbps by viewModel.currentDownloadSpeedKbps.collectAsStateWithLifecycle()
    val uploadKbps by viewModel.currentUploadSpeedKbps.collectAsStateWithLifecycle()
    val currentPingMs by viewModel.currentPingMs.collectAsStateWithLifecycle()
    val durationSeconds by viewModel.connectionDurationSeconds.collectAsStateWithLifecycle()

    val uploadPoints by viewModel.uploadPoints.collectAsStateWithLifecycle()
    val downloadPoints by viewModel.downloadPoints.collectAsStateWithLifecycle()
    val todayUploadBytes by viewModel.todayUploadBytes.collectAsStateWithLifecycle()
    val todayDownloadBytes by viewModel.todayDownloadBytes.collectAsStateWithLifecycle()

    val favoriteServers = remember(servers) { servers.filter { it.isFavorite } }

    Crossfade(targetState = currentDestination, label = "ScreenTransition") { destination ->
        when (destination) {
            AppDestination.SPLASH -> {
                SplashScreen(
                    onSplashFinished = {
                        if (settings.isPinLockEnabled && !isPinUnlocked) {
                            currentDestination = AppDestination.PIN_LOCK
                        } else if (currentUser != null) {
                            currentDestination = AppDestination.MAIN
                        } else {
                            currentDestination = AppDestination.AUTH
                        }
                    }
                )
            }

            AppDestination.PIN_LOCK -> {
                PinLockScreen(
                    correctPin = if (settings.pinCode.isNotEmpty()) settings.pinCode else "1234",
                    onUnlockSuccess = {
                        isPinUnlocked = true
                        currentDestination = if (currentUser != null) AppDestination.MAIN else AppDestination.AUTH
                    }
                )
            }

            AppDestination.AUTH -> {
                AuthScreen(
                    onAuthSuccess = { currentDestination = AppDestination.MAIN },
                    onLoginClick = { email, pass, rem -> },
                    onRegisterClick = { un, email, pass -> },
                    onForgotPasswordClick = { email -> }
                )
            }

            AppDestination.SETTINGS -> {
                SettingsScreen(
                    settings = settings,
                    onBackClick = { currentDestination = AppDestination.MAIN },
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onThemeChange = { viewModel.updateTheme(it) },
                    onProxyModeChange = { viewModel.updateProxyMode(it) },
                    onDnsChange = { viewModel.updateDnsServer(it) },
                    onMtuChange = { viewModel.updateMtuSize(it) },
                    onSecurityChange = { bio, pin, code, screen ->
                        viewModel.updateSecuritySettings(bio, pin, code, screen)
                    }
                )
            }

            AppDestination.MAIN -> {
                Scaffold(
                    topBar = {
                        VIPTopBar(
                            currentLanguage = settings.language,
                            onLanguageToggle = { viewModel.toggleLanguage() },
                            onSettingsClick = { currentDestination = AppDestination.SETTINGS }
                        )
                    },
                    bottomBar = {
                        VIPBottomNavigation(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it }
                        )
                    },
                    containerColor = DarkBackground
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentTab) {
                            BottomTab.HOME -> {
                                HomeScreen(
                                    connectionState = connectionState,
                                    currentServer = selectedServer,
                                    currentPingMs = currentPingMs,
                                    downloadKbps = downloadKbps,
                                    uploadKbps = uploadKbps,
                                    durationSeconds = durationSeconds,
                                    todayUploadBytes = todayUploadBytes,
                                    todayDownloadBytes = todayDownloadBytes,
                                    favoriteServers = favoriteServers,
                                    onToggleConnection = { viewModel.toggleConnection() },
                                    onSelectServerClick = { currentTab = BottomTab.SERVERS },
                                    onSelectServer = { viewModel.selectServer(it) }
                                )
                            }

                            BottomTab.SERVERS -> {
                                ServersScreen(
                                    servers = servers,
                                    selectedServerId = selectedServer?.id,
                                    onSelectServer = {
                                        viewModel.selectServer(it)
                                        currentTab = BottomTab.HOME
                                    },
                                    onToggleFavorite = { viewModel.toggleServerFavorite(it) }
                                )
                            }

                            BottomTab.CONFIGS -> {
                                ConfigsScreen(
                                    configs = configs,
                                    activeConfigId = activeConfig?.id ?: configs.firstOrNull()?.id,
                                    onSelectConfig = { viewModel.selectConfig(it) },
                                    onToggleFavorite = { viewModel.toggleConfigFavorite(it) },
                                    onDeleteConfig = { viewModel.deleteConfig(it) },
                                    onImportLink = { viewModel.importConfigLink(it) }
                                )
                            }

                            BottomTab.STATISTICS -> {
                                StatisticsScreen(
                                    uploadPoints = uploadPoints,
                                    downloadPoints = downloadPoints,
                                    avgPingMs = currentPingMs,
                                    todayUploadBytes = todayUploadBytes,
                                    todayDownloadBytes = todayDownloadBytes
                                )
                            }

                            BottomTab.PROFILE -> {
                                ProfileScreen(
                                    currentUser = currentUser,
                                    onLogout = {
                                        viewModel.logout()
                                        currentDestination = AppDestination.AUTH
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
