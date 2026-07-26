package com.example.util

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import java.io.File
import java.security.MessageDigest

object SecurityUtils {

    fun isDeviceRooted(): Boolean {
        return checkBuildTags() || checkSuPaths() || checkTestKeys()
    }

    private fun checkBuildTags(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkSuPaths(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { File(it).exists() }
    }

    private fun checkTestKeys(): Boolean {
        return try {
            val file = File("/system/app/Superuser.apk")
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    fun hashPin(pin: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(pin.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun applyScreenshotProtection(activity: Activity, enable: Boolean) {
        if (enable) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
