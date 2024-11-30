package com.capstone.arabicmorph.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.capstone.arabicmorph.gamification.database.GamDatabase
import com.capstone.arabicmorph.gamification.util.NotificationUtils
import com.capstone.arabicmorph.gamification.util.UserDataManager
import java.util.*

class ChallengeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val gamDatabase = GamDatabase.getInstance(applicationContext)
    private val learnedWordDao = gamDatabase.learnedWordDao()
    private val userDataManager = UserDataManager(applicationContext)
    private val notificationUtils = NotificationUtils()

    override suspend fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val startOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        val endOfDay = calendar.apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        val learnedWordsCount = learnedWordDao.getLearnedWordsCountToday(startOfDay, endOfDay)

        if (learnedWordsCount >= 5) {
            userDataManager.addXp(10)
            notificationUtils.sendChallengeCompletedNotification(applicationContext)
            return Result.success()
        }

        return Result.failure()
    }
}
