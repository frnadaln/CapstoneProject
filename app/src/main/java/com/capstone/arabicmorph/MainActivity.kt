package com.capstone.arabicmorph

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, VerbConjugatorFragment.DrawerToggleListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener(this)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, VerbConjugatorFragment())
            .commit()
    }

    override fun onMenuIconClicked() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

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
