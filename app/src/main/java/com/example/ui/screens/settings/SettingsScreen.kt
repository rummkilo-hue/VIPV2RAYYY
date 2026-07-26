package com.example.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.AppLanguage
import com.example.domain.model.AppSettings
import com.example.domain.model.AppTheme
import com.example.domain.model.ProxyMode
import com.example.ui.theme.*
import com.example.util.SecurityUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onBackClick: () -> Unit,
    onLanguageToggle: () -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onProxyModeChange: (ProxyMode) -> Unit,
    onDnsChange: (String) -> Unit,
    onMtuChange: (Int) -> Unit,
    onSecurityChange: (biometric: Boolean, pinLock: Boolean, pinCode: String, screenshotProtect: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf(settings.pinCode) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val isRooted = remember { SecurityUtils.isDeviceRooted() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), color = TextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: General & Localization
            Text("General & Language", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(cornerRadius = 18.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Language Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(stringResource(R.string.language), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(settings.language.displayName, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = onLanguageToggle,
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            border = BorderStroke(1.dp, GlassBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (settings.language == AppLanguage.KHMER) "🇬🇧 Switch EN" else "🇰🇭 ដូរជាភាសាខ្មែរ", color = TextPrimary, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // App Theme Selector
                    Column {
                        Text("App Theme / ប្រធានបទ", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AppTheme.values().forEach { theme ->
                                val isSelected = settings.theme == theme
                                val themeLabel = when (theme) {
                                    AppTheme.KHMER_ANGKOR -> "🇰🇭 Angkor"
                                    AppTheme.AMOLED_DARK -> "🌌 AMOLED"
                                    AppTheme.GLASS_DARK -> "💎 Glass"
                                    AppTheme.MIDNIGHT_BLUE -> "🔷 Blue"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) AngkorGold else DarkSurfaceVariant)
                                        .clickable { onThemeChange(theme) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = themeLabel,
                                        color = if (isSelected) Color(0xFF2A1A0E) else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // Proxy Mode
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Router, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(stringResource(R.string.proxy_mode), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ProxyMode.values().forEach { mode ->
                                val isSelected = settings.proxyMode == mode
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) NeonCyan else DarkSurfaceVariant)
                                        .clickable { onProxyModeChange(mode) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = mode.name,
                                        color = if (isSelected) ElectricPurple else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Network & DNS
            Text("Network Configurations", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(cornerRadius = 18.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // DNS Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Dns, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(stringResource(R.string.dns_settings), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(settings.dnsServer, color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // MTU Size
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.mtu_size), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("${settings.mtuSize} Bytes", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = settings.mtuSize.toFloat(),
                            onValueChange = { onMtuChange(it.toInt()) },
                            valueRange = 1280f..1500f,
                            steps = 22,
                            colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                        )
                    }
                }
            }

            // Section 3: Security & App Lock
            Text("Security & Privacy", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(cornerRadius = 18.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Biometric Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Biometric Fingerprint Lock", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Switch(
                            checked = settings.isBiometricEnabled,
                            onCheckedChange = { enabled ->
                                onSecurityChange(enabled, settings.isPinLockEnabled, settings.pinCode, settings.isScreenshotProtected)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // PIN Lock Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(stringResource(R.string.pin_lock), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Switch(
                            checked = settings.isPinLockEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled) showPinDialog = true
                                else onSecurityChange(settings.isBiometricEnabled, false, "", settings.isScreenshotProtected)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // Screenshot Protection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(stringResource(R.string.screenshot_protection), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Switch(
                            checked = settings.isScreenshotProtected,
                            onCheckedChange = { enabled ->
                                onSecurityChange(settings.isBiometricEnabled, settings.isPinLockEnabled, settings.pinCode, enabled)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                        )
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    // Root Detection Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BugReport, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(stringResource(R.string.root_detection), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isRooted) StatusRed.copy(alpha = 0.2f) else StatusGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isRooted) "ROOTED" else "SAFE",
                                color = if (isRooted) StatusRed else StatusGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Section 4: About & Privacy
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(cornerRadius = 18.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(stringResource(R.string.about), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }

                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(stringResource(R.string.privacy_policy), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Set Security PIN", color = TextPrimary) },
            text = {
                Column {
                    Text("Enter 4-digit PIN code:", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 4) pinInput = it },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("pin_code_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput.length == 4) {
                            onSecurityChange(settings.isBiometricEnabled, true, pinInput, settings.isScreenshotProtected)
                            showPinDialog = false
                            Toast.makeText(context, "PIN Lock enabled", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Save PIN", color = ElectricPurple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About VIPV2RAY", color = TextPrimary) },
            text = {
                Text(
                    "VIPV2RAY v2.4.0 Pro\n\nA high-speed V2Ray client supporting VLESS, VMess, Trojan, Hysteria2, TUIC, and Reality protocols. Built with modern Kotlin, Jetpack Compose, and Material 3 design.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)) {
                    Text("Close", color = ElectricPurple)
                }
            },
            containerColor = DarkSurface
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy", color = TextPrimary) },
            text = {
                Text(
                    "Zero Logs Policy:\n\nVIPV2RAY does NOT log user traffic, DNS queries, IP addresses, or connection timestamps. All local configurations and secrets are stored in encrypted app storage.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(onClick = { showPrivacyDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)) {
                    Text("OK", color = ElectricPurple)
                }
            },
            containerColor = DarkSurface
        )
    }
}
