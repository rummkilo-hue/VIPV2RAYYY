package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.remote.AdminApiService
import com.example.data.remote.AuthInterceptor
import com.example.data.repository.*
import com.example.domain.model.*
import com.example.ui.components.ConnectionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Locale
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getInstance(application)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_jwt_token_kh" })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.vipv2ray.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiService = retrofit.create(AdminApiService::class.java)

    val serverRepository = ServerRepository(database.serverDao(), apiService)
    val configRepository = ConfigRepository(database.configDao())
    val trafficRepository = TrafficRepository(database.trafficStatDao())
    val authRepository = AuthRepository(apiService, userPreferences)

    // App Settings State
    val settingsState: StateFlow<AppSettings> = userPreferences.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    // Servers & Configs State
    val serversState: StateFlow<List<Server>> = serverRepository.allServersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val configsState: StateFlow<List<ProxyConfig>> = configRepository.allConfigsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUserState: StateFlow<User?> = authRepository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Connection State
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _selectedServer = MutableStateFlow<Server?>(null)
    val selectedServer: StateFlow<Server?> = _selectedServer.asStateFlow()

    private val _activeConfig = MutableStateFlow<ProxyConfig?>(null)
    val activeConfig: StateFlow<ProxyConfig?> = _activeConfig.asStateFlow()

    // Metrics State
    private val _currentDownloadSpeedKbps = MutableStateFlow(0f)
    val currentDownloadSpeedKbps: StateFlow<Float> = _currentDownloadSpeedKbps.asStateFlow()

    private val _currentUploadSpeedKbps = MutableStateFlow(0f)
    val currentUploadSpeedKbps: StateFlow<Float> = _currentUploadSpeedKbps.asStateFlow()

    private val _currentPingMs = MutableStateFlow(24)
    val currentPingMs: StateFlow<Int> = _currentPingMs.asStateFlow()

    private val _connectionDurationSeconds = MutableStateFlow(0L)
    val connectionDurationSeconds: StateFlow<Long> = _connectionDurationSeconds.asStateFlow()

    // Speed Graph Points
    private val _uploadPoints = MutableStateFlow(listOf(10f, 15f, 25f, 20f, 30f, 18f, 22f, 35f, 28f, 40f))
    val uploadPoints: StateFlow<List<Float>> = _uploadPoints.asStateFlow()

    private val _downloadPoints = MutableStateFlow(listOf(120f, 250f, 480f, 390f, 620f, 850f, 920f, 1100f, 1050f, 1250f))
    val downloadPoints: StateFlow<List<Float>> = _downloadPoints.asStateFlow()

    // Today Traffic Totals
    private val _todayUploadBytes = MutableStateFlow(240_000_000L)
    val todayUploadBytes: StateFlow<Long> = _todayUploadBytes.asStateFlow()

    private val _todayDownloadBytes = MutableStateFlow(1_850_000_000L)
    val todayDownloadBytes: StateFlow<Long> = _todayDownloadBytes.asStateFlow()

    private var trafficJob: Job? = null

    init {
        viewModelScope.launch {
            serverRepository.ensureDefaultServers()
            configRepository.ensureDefaultConfigs()
            
            // Set initial selected server
            serversState.firstOrNull { it.isNotEmpty() }?.let { list ->
                _selectedServer.value = list.firstOrNull { it.isRecommended } ?: list.firstOrNull()
            }
        }
    }

    fun toggleConnection() {
        when (_connectionState.value) {
            ConnectionState.DISCONNECTED -> {
                viewModelScope.launch {
                    _connectionState.value = ConnectionState.CONNECTING
                    delay(1200)
                    _connectionState.value = ConnectionState.CONNECTED
                    startTrafficMonitoring()
                }
            }
            ConnectionState.CONNECTED -> {
                viewModelScope.launch {
                    _connectionState.value = ConnectionState.DISCONNECTING
                    delay(800)
                    _connectionState.value = ConnectionState.DISCONNECTED
                    stopTrafficMonitoring()
                }
            }
            else -> {}
        }
    }

    private fun startTrafficMonitoring() {
        trafficJob?.cancel()
        trafficJob = viewModelScope.launch {
            var duration = 0L
            while (_connectionState.value == ConnectionState.CONNECTED) {
                delay(1000)
                duration++
                _connectionDurationSeconds.value = duration

                val dlSpeed = Random.nextFloat() * 1200f + 300f
                val ulSpeed = Random.nextFloat() * 250f + 50f
                val ping = Random.nextInt(18, 42)

                _currentDownloadSpeedKbps.value = dlSpeed
                _currentUploadSpeedKbps.value = ulSpeed
                _currentPingMs.value = ping

                // Update graph points
                _downloadPoints.value = (_downloadPoints.value.drop(1) + dlSpeed)
                _uploadPoints.value = (_uploadPoints.value.drop(1) + ulSpeed)

                // Accumulate usage
                _todayDownloadBytes.value += (dlSpeed * 128).toLong()
                _todayUploadBytes.value += (ulSpeed * 128).toLong()

                if (duration % 5 == 0L) {
                    val serverId = _selectedServer.value?.id ?: "sg-vip-01"
                    val protocol = _activeConfig.value?.protocol?.name ?: "VLESS"
                    trafficRepository.recordTrafficSample((ulSpeed * 128).toLong(), (dlSpeed * 128).toLong(), ping, serverId, protocol)
                }
            }
        }
    }

    private fun stopTrafficMonitoring() {
        trafficJob?.cancel()
        _currentDownloadSpeedKbps.value = 0f
        _currentUploadSpeedKbps.value = 0f
        _connectionDurationSeconds.value = 0L
    }

    fun selectServer(server: Server) {
        _selectedServer.value = server
    }

    fun toggleServerFavorite(server: Server) {
        viewModelScope.launch {
            serverRepository.toggleFavorite(server.id, !server.isFavorite)
        }
    }

    fun selectConfig(config: ProxyConfig) {
        viewModelScope.launch {
            configRepository.setActiveConfig(config.id)
            _activeConfig.value = config
        }
    }

    fun toggleConfigFavorite(config: ProxyConfig) {
        viewModelScope.launch {
            configRepository.toggleFavorite(config.id, !config.isFavorite)
        }
    }

    fun deleteConfig(config: ProxyConfig) {
        viewModelScope.launch {
            configRepository.deleteConfig(config.id)
        }
    }

    fun importConfigLink(link: String): Boolean {
        val config = configRepository.parseConfigLink(link) ?: return false
        viewModelScope.launch {
            configRepository.addConfig(config)
        }
        return true
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val current = settingsState.value.language
            val newLang = if (current == AppLanguage.KHMER) AppLanguage.ENGLISH else AppLanguage.KHMER
            userPreferences.saveLanguage(newLang)
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            userPreferences.saveTheme(theme)
        }
    }

    fun updateProxyMode(mode: ProxyMode) {
        viewModelScope.launch {
            userPreferences.saveProxyMode(mode)
        }
    }

    fun updateDnsServer(dns: String) {
        viewModelScope.launch {
            userPreferences.saveDnsServer(dns)
        }
    }

    fun updateMtuSize(mtu: Int) {
        viewModelScope.launch {
            userPreferences.saveMtuSize(mtu)
        }
    }

    fun updateSecuritySettings(biometric: Boolean, pinLock: Boolean, pinCode: String, screenshotProtect: Boolean) {
        viewModelScope.launch {
            userPreferences.saveSecuritySettings(biometric, pinLock, pinCode, screenshotProtect)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
