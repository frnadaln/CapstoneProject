package com.capstone.arabicmorph

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, VerbConjugatorFragment.DrawerToggleListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customizeStatusBar()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, VerbConjugatorFragment())
            .commit()
    }

    private fun customizeStatusBar() {
        @Suppress("DEPRECATION")
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onMenuIconClicked() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_verb_conjugator -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, VerbConjugatorFragment())
                    .commit()
            }
            R.id.nav_jamid_detector -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, JamidDetectorFragment())
                    .commit()
            }
            R.id.nav_setting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, SettingFragment())
                    .commit()
            }
            R.id.nav_app_info -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, AppInfoFragment())
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
