package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.AppLanguage
import com.example.domain.model.AppSettings
import com.example.domain.model.AppTheme
import com.example.domain.model.ProxyMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vipv2ray_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_PROXY_MODE = stringPreferencesKey("proxy_mode")
        val KEY_DNS_SERVER = stringPreferencesKey("dns_server")
        val KEY_MTU_SIZE = intPreferencesKey("mtu_size")
        val KEY_BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val KEY_PIN_LOCK_ENABLED = booleanPreferencesKey("pin_lock_enabled")
        val KEY_PIN_CODE = stringPreferencesKey("pin_code")
        val KEY_SCREENSHOT_PROTECTED = booleanPreferencesKey("screenshot_protected")
        val KEY_ROOT_DETECTION_ENABLED = booleanPreferencesKey("root_detection_enabled")
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_REMEMBER_LOGIN = booleanPreferencesKey("remember_login")
        val KEY_ACTIVE_SERVER_ID = stringPreferencesKey("active_server_id")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        val langCode = prefs[KEY_LANGUAGE] ?: AppLanguage.KHMER.code
        val language = if (langCode == AppLanguage.ENGLISH.code) AppLanguage.ENGLISH else AppLanguage.KHMER

        val themeStr = prefs[KEY_THEME] ?: AppTheme.AMOLED_DARK.name
        val theme = runCatching { AppTheme.valueOf(themeStr) }.getOrDefault(AppTheme.AMOLED_DARK)

        val modeStr = prefs[KEY_PROXY_MODE] ?: ProxyMode.GLOBAL.name
        val mode = runCatching { ProxyMode.valueOf(modeStr) }.getOrDefault(ProxyMode.GLOBAL)

        AppSettings(
            language = language,
            theme = theme,
            proxyMode = mode,
            dnsServer = prefs[KEY_DNS_SERVER] ?: "1.1.1.1 (Cloudflare Secure)",
            mtuSize = prefs[KEY_MTU_SIZE] ?: 1400,
            isBiometricEnabled = prefs[KEY_BIOMETRIC_ENABLED] ?: false,
            isPinLockEnabled = prefs[KEY_PIN_LOCK_ENABLED] ?: false,
            pinCode = prefs[KEY_PIN_CODE] ?: "",
            isScreenshotProtected = prefs[KEY_SCREENSHOT_PROTECTED] ?: true,
            isRootDetectionEnabled = prefs[KEY_ROOT_DETECTION_ENABLED] ?: true,
            isNotificationEnabled = prefs[KEY_NOTIFICATIONS_ENABLED] ?: true
        )
    }

    val authTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTH_TOKEN]
    }

    val activeServerIdFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ACTIVE_SERVER_ID]
    }

    suspend fun saveLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.code
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme.name
        }
    }

    suspend fun saveProxyMode(proxyMode: ProxyMode) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PROXY_MODE] = proxyMode.name
        }
    }

    suspend fun saveDnsServer(dns: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DNS_SERVER] = dns
        }
    }

    suspend fun saveMtuSize(mtu: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MTU_SIZE] = mtu
        }
    }

    suspend fun saveSecuritySettings(
        biometricEnabled: Boolean,
        pinLockEnabled: Boolean,
        pinCode: String,
        screenshotProtected: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_BIOMETRIC_ENABLED] = biometricEnabled
            prefs[KEY_PIN_LOCK_ENABLED] = pinLockEnabled
            prefs[KEY_PIN_CODE] = pinCode
            prefs[KEY_SCREENSHOT_PROTECTED] = screenshotProtected
        }
    }

    suspend fun saveAuthToken(token: String?, email: String?, name: String?, rememberLogin: Boolean) {
        context.dataStore.edit { prefs ->
            if (token != null) prefs[KEY_AUTH_TOKEN] = token else prefs.remove(KEY_AUTH_TOKEN)
            if (email != null) prefs[KEY_USER_EMAIL] = email else prefs.remove(KEY_USER_EMAIL)
            if (name != null) prefs[KEY_USER_NAME] = name else prefs.remove(KEY_USER_NAME)
            prefs[KEY_REMEMBER_LOGIN] = rememberLogin
        }
    }

    suspend fun setActiveServerId(serverId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACTIVE_SERVER_ID] = serverId
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
            prefs.remove(KEY_USER_EMAIL)
            prefs.remove(KEY_USER_NAME)
        }
    }
}
