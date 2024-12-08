package com.capstone.arabicmorph.gamification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.capstone.arabicmorph.worker.ChallengeWorker
import java.util.*
import java.util.concurrent.TimeUnit

class DailyChallengeScheduler {
    fun scheduleDailyChallenge(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val tag = "daily_challenge"

        // Batalkan pekerjaan lama jika ada
        workManager.cancelAllWorkByTag(tag)

        val workRequest = OneTimeWorkRequestBuilder<ChallengeWorker>()
            .setInitialDelay(calculateNextMidnight(), TimeUnit.MILLISECONDS)
            .addTag(tag)
            .build()

        workManager.enqueue(workRequest)
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
