package com.miszczyk.passlingo.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import com.miszczyk.passlingo.MainActivity
import com.miszczyk.passlingo.ui.screens.home.data.TimeAndAppsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppBlockAccessibilityService : AccessibilityService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var repository: TimeAndAppsRepository

    @Volatile private var currentForegroundPackage: String? = null


    @Volatile private var isScreenOn = true
    private var lastBlockedPackage: String? = null

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isScreenOn = intent.action == Intent.ACTION_SCREEN_ON
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
            flags = 0
        }
        serviceInfo = info

        Log.d("A11yService", "onServiceConnected, serviceInfo=$serviceInfo")

        repository = TimeAndAppsRepository(applicationContext)

        ContextCompat.registerReceiver(
            this,
            screenStateReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        startMonitoring()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d("A11yService", "Foreground changed: ${event.packageName}")
            event.packageName?.toString()?.let { currentForegroundPackage = it }
        }
    }

    private fun startMonitoring() {
        scope.launch {
            while (isActive) {
                val lockedApps = repository.lockedApps.first()
                Log.d("A11yService", "lockedApps=$lockedApps, current=$currentForegroundPackage, screenOn=$isScreenOn")

                if (!isScreenOn || lockedApps.isEmpty()) {
                    delay(1000)
                    continue
                }

                val currentAppPackage = currentForegroundPackage
                val currentBalance = repository.balanceTime.first()

                when {
                    currentAppPackage != null && lockedApps.contains(currentAppPackage) -> {
                        if (currentBalance > 0) {
                            repository.consumeCreditTime(secondsLost = 1L)
                            lastBlockedPackage = null
                        } else if (currentAppPackage != lastBlockedPackage) {
                            lastBlockedPackage = currentAppPackage
                            blockApplication(currentAppPackage)
                        }
                    }
                    else -> lastBlockedPackage = null
                }
                delay(1000)
            }
        }
    }

    private fun blockApplication(packageName: String) {
        val lockIntent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("BLOCKED_PACKAGE", packageName)
        }
        startActivity(lockIntent)
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenStateReceiver)
        scope.cancel()
    }
}