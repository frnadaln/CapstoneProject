package com.capstone.arabicmorph.view.setting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.capstone.arabicmorph.R
import android.widget.Switch
import android.widget.Toast
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment(R.layout.fragment_setting) {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var notificationSwitch: Switch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationSwitch = view.findViewById(R.id.notification_switch)

        val isNotificationEnabled = getNotificationStatus()
        notificationSwitch.isChecked = isNotificationEnabled

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleReminder()
                saveNotificationStatus(true)
                Toast.makeText(requireContext(), "Notifications turned on", Toast.LENGTH_SHORT).show()
            } else {
                cancelReminder()
                saveNotificationStatus(false)
                Toast.makeText(requireContext(), "Notifications turned off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleReminder() {
        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .addTag("reminder")
            .build()

        WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
    }

    private fun cancelReminder() {
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag("reminder")
    }

    private fun saveNotificationStatus(enabled: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("notification_enabled", enabled)
            apply()
        }
    }

    private fun getNotificationStatus(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("notification_enabled", false)
    }
}
