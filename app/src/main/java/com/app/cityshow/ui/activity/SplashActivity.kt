package com.app.cityshow.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.cityshow.R
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Log

class SplashActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        initUi()
    }

    override fun initUi() {

        Handler(Looper.getMainLooper()).postDelayed({
            if (!LocalDataHelper.login) {
                openLoginActivity()
            } else {
                openHomeActivity()
            }
        }, 1000)
    }
}