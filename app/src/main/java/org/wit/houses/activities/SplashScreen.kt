package org.wit.houses.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.wit.houses.R
import org.wit.houses.views.houselist.HouseListView
import org.wit.houses.views.login.LoginView

class SplashScreen : AppCompatActivity() {
    private val splashTimeout = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginView::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout.toLong())
    }
}