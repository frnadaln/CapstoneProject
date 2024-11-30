package com.capstone.arabicmorph.gamification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.capstone.arabicmorph.worker.ChallengeWorker
import java.util.*
import java.util.concurrent.TimeUnit

class DailyChallengeScheduler {
    fun scheduleDailyChallenge(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<ChallengeWorker>()
            .setInitialDelay(calculateNextMidnight(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun calculateNextMidnight(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis - System.currentTimeMillis()
    }
}
