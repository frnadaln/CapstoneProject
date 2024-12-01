package com.capstone.arabicmorph

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.capstone.arabicmorph.view.history.HistoryFragment
import com.capstone.arabicmorph.view.jamiddetector.JamidDetectorFragment
import com.capstone.arabicmorph.worker.ReminderWorker
import com.capstone.arabicmorph.view.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customizeStatusBar()

        scheduleReminder()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_verb_conjugator -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, VerbConjugatorFragment())
                        .commit()
                    toolbar.title = "Verb Conjugator"
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, HistoryFragment())
                        .commit()
                    toolbar.title = "History"
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_jamid_detector -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, JamidDetectorFragment())
                        .commit()
                    toolbar.title = "Jamid Detector"
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, VerbConjugatorFragment())
                .commit()

            toolbar.title = "Verb Conjugator"

            bottomNavigationView.selectedItemId = R.id.nav_verb_conjugator
        }

        toolbar.setNavigationIcon(R.drawable.icon_menu)
        toolbar.setNavigationOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            } else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun customizeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun scheduleReminder() {
        val workManager = WorkManager.getInstance(this)
        workManager.getWorkInfosByTag("reminder")
            .get()
            .let { workInfos ->
                val isWorkScheduled = workInfos?.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING } == true
                if (!isWorkScheduled) {
                    val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                        .addTag("reminder")
                        .build()
                    workManager.enqueue(reminderRequest)
                }
            }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_verb_conjugator -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, VerbConjugatorFragment())
                    .commit()
                toolbar.title = "Verb Conjugator"
                bottomNavigationView.visibility = View.VISIBLE
            }
            R.id.nav_jamid_detector -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, JamidDetectorFragment())
                    .commit()
                toolbar.title = "Jamid Detector"
                bottomNavigationView.visibility = View.VISIBLE
            }
            R.id.nav_setting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, SettingFragment())
                    .commit()
                toolbar.title = "Setting"
                bottomNavigationView.visibility = View.VISIBLE
            }
            R.id.nav_app_info -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, AppInfoFragment())
                    .commit()
                toolbar.title = "App Info"
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications authorization granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications authorization rejected", Toast.LENGTH_SHORT).show()
            }
        }
}
