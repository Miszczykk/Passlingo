package com.miszczyk.passlingo.ui.util

import com.miszczyk.passlingo.ui.screens.home.util.Constants.EARN_TIME_SECONDS
import java.util.Locale

fun formatTime(totalSeconds: Long, forceFullFormat: Boolean = true): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        forceFullFormat || hours > 0L -> String.format(
            Locale.US, format = "%02dh %02dm %02ds", hours, minutes, seconds
        )

        minutes > 0L -> String.format(Locale.US, format = "%02dm %02ds", minutes, seconds)
        else -> String.format(Locale.US, format = "%02ds", seconds)
    }
}

fun earnedTimeFor(numberOfApplication: Int): Long {
    return EARN_TIME_SECONDS * numberOfApplication
}