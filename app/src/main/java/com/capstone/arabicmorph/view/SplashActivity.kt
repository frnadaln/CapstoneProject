package com.capstone.arabicmorph.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.capstone.arabicmorph.MainActivity
import com.capstone.arabicmorph.R

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Mulai animasi bounce untuk logo
        val splashLogo = findViewById<ImageView>(R.id.splash_logo)
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        splashLogo.startAnimation(bounceAnimation)

        // Delay untuk berpindah ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Tambahkan animasi transisi
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            finish()
        }, 2000) // Delay 2 detik
    }
}
