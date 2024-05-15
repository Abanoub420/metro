package com.example.metrov6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.ybq.android.spinkit.style.Circle

class splash_activity : AppCompatActivity() {
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Thread.sleep(3000)
//        installSplashScreen()
            setContentView(R.layout.activity_splash)
            showSplashScreen()
        progressbar = findViewById(R.id.spin_kit_splash) as ProgressBar

        val spinKitDrawable = Circle()

        progressbar.indeterminateDrawable = Circle()
        spinKitDrawable.color = resources.getColor(R.color.black)

    }
    fun showSplashScreen() {
        val splash_img = findViewById<ImageView>(R.id.splash_img)
        splash_img.alpha = 0f
        splash_img.animate().setDuration(3000).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            overridePendingTransition(androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom, androidx.appcompat.R.anim.abc_fade_in)
            finish()
        }
//        splash_img.animate().setDuration(3000).alpha(1f).withStartAction() {
////            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            overridePendingTransition(androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom, androidx.appcompat.R.anim.abc_fade_in)
//
//        }

    }

}
