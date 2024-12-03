package com.capstone.arabicmorph.view

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
import androidx.fragment.app.Fragment
import com.capstone.arabicmorph.view.history.HistoryFragment
import com.capstone.arabicmorph.view.jamiddetector.JamidDetectorFragment
import com.capstone.arabicmorph.view.setting.SettingFragment
import com.capstone.arabicmorph.worker.ReminderWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.view.appinfo.AppInfoFragment
import com.capstone.arabicmorph.view.verbconjugator.VerbConjugatorFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customizeStatusBar()
        scheduleReminder()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            handleBottomNavigation(item)
        }

        if (savedInstanceState == null) {
            replaceFragment(VerbConjugatorFragment(), "Verb Conjugator")
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

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarTitle()
            updateNavigationIndicators()
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
        val workInfos = workManager.getWorkInfosByTag("reminder").get()
        val isWorkScheduled = workInfos?.any {
            it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING
        } == true

        if (!isWorkScheduled) {
            val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .addTag("reminder")
                .build()
            workManager.enqueue(reminderRequest)
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_content)
        if (currentFragment?.javaClass == fragment.javaClass) return // Avoid redundant replacements

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.main_content, fragment)
            .addToBackStack(title)
            .commit()

        toolbar.title = title
    }

    private fun handleBottomNavigation(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_verb_conjugator -> {
                replaceFragment(VerbConjugatorFragment(), "Verb Conjugator")
                return true
            }
            R.id.nav_history -> {
                replaceFragment(HistoryFragment(), "History")
                return true
            }
            R.id.nav_jamid_detector -> {
                replaceFragment(JamidDetectorFragment(), "Jamid Detector")
                return true
            }
        }
        return false
    }

    private fun updateToolbarTitle() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_content)
        toolbar.title = when (currentFragment) {
            is VerbConjugatorFragment -> "Verb Conjugator"
            is HistoryFragment -> "History"
            is JamidDetectorFragment -> "Jamid Detector"
            is SettingFragment -> "Setting"
            is AppInfoFragment -> "App Info"
            else -> ""
        }
    }

    private fun updateNavigationIndicators() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_content)

        when (currentFragment) {
            is VerbConjugatorFragment -> {
                bottomNavigationView.selectedItemId = R.id.nav_verb_conjugator
                navigationView.setCheckedItem(R.id.nav_verb_conjugator)
            }
            is HistoryFragment -> {
                bottomNavigationView.selectedItemId = R.id.nav_history
                navigationView.setCheckedItem(R.id.nav_history)
            }
            is JamidDetectorFragment -> {
                bottomNavigationView.selectedItemId = R.id.nav_jamid_detector
                navigationView.setCheckedItem(R.id.nav_jamid_detector)
            }
            is SettingFragment -> {
                navigationView.setCheckedItem(R.id.nav_setting)
            }
            is AppInfoFragment -> {
                navigationView.setCheckedItem(R.id.nav_app_info)
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
            R.id.nav_verb_conjugator -> replaceFragment(VerbConjugatorFragment(), "Verb Conjugator")
            R.id.nav_history -> replaceFragment(HistoryFragment(), "History")
            R.id.nav_jamid_detector -> replaceFragment(JamidDetectorFragment(), "Jamid Detector")
            R.id.nav_setting -> replaceFragment(SettingFragment(), "Setting")
            R.id.nav_app_info -> replaceFragment(AppInfoFragment(), "App Info")
        }
        navigationView.setCheckedItem(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val message = if (isGranted) {
                "Notifications authorization granted"
            } else {
                "Notifications authorization rejected"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
}
