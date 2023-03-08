package com.app.cityshow.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app.cityshow.R
import com.app.cityshow.utility.LocalDataHelper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        initUi()
    }

    fun initUi() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!LocalDataHelper.login) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
            }
        }, 1000)
    }
}