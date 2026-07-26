package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

enum class AuthTab { LOGIN, REGISTER, FORGOT_PASSWORD }

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onLoginClick: (String, String, Boolean) -> Unit,
    onRegisterClick: (String, String, String) -> Unit,
    onForgotPasswordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(AuthTab.LOGIN) }
    var email by remember { mutableStateOf("vip.user@vipv2ray.com") }
    var password by remember { mutableStateOf("vip123456") }
    var username by remember { mutableStateOf("VIP Member") }
    var rememberLogin by remember { mutableStateOf(true) }
    var infoMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassCard(cornerRadius = 24.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VIPV2RAY",
                color = NeonCyan,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (activeTab) {
                    AuthTab.LOGIN -> stringResource(R.string.login_title)
                    AuthTab.REGISTER -> stringResource(R.string.register_title)
                    AuthTab.FORGOT_PASSWORD -> stringResource(R.string.forgot_password)
                },
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (infoMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(StatusGreen.copy(alpha = 0.2f))
                        .padding(12.dp)
                ) {
                    Text(infoMessage, color = StatusGreen, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            AnimatedVisibility(visible = activeTab == AuthTab.REGISTER) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_username_input")
                )
            }

            if (activeTab == AuthTab.REGISTER) Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label), color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = GlassBorder
                ),
                modifier = Modifier.fillMaxWidth().testTag("auth_email_input")
            )

            if (activeTab != AuthTab.FORGOT_PASSWORD) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password_label), color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input")
                )
            }

            if (activeTab == AuthTab.LOGIN) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberLogin,
                            onCheckedChange = { rememberLogin = it },
                            colors = CheckboxDefaults.colors(checkedColor = NeonCyan)
                        )
                        Text(
                            text = stringResource(R.string.remember_login),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { activeTab = AuthTab.FORGOT_PASSWORD }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    when (activeTab) {
                        AuthTab.LOGIN -> {
                            onLoginClick(email, password, rememberLogin)
                            onAuthSuccess()
                        }
                        AuthTab.REGISTER -> {
                            onRegisterClick(username, email, password)
                            onAuthSuccess()
                        }
                        AuthTab.FORGOT_PASSWORD -> {
                            onForgotPasswordClick(email)
                            infoMessage = "Password reset email sent to $email"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("auth_submit_button")
            ) {
                Text(
                    text = when (activeTab) {
                        AuthTab.LOGIN -> stringResource(R.string.login_button)
                        AuthTab.REGISTER -> stringResource(R.string.register_button)
                        AuthTab.FORGOT_PASSWORD -> "Send Reset Link"
                    },
                    color = ElectricPurple,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (activeTab == AuthTab.LOGIN) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { onAuthSuccess() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                    border = BorderStroke(1.dp, NeonCyan),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.biometric_login), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = when (activeTab) {
                    AuthTab.LOGIN -> stringResource(R.string.no_account)
                    AuthTab.REGISTER, AuthTab.FORGOT_PASSWORD -> stringResource(R.string.has_account)
                },
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    activeTab = if (activeTab == AuthTab.LOGIN) AuthTab.REGISTER else AuthTab.LOGIN
                    infoMessage = ""
                }
            )
        }
    }
}
