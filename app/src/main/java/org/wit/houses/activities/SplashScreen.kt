package org.wit.houses.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.wit.houses.R

class SplashScreen : AppCompatActivity() {
    private val splashTimeout = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent = Intent(this, HouseListActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout.toLong())
    }
}