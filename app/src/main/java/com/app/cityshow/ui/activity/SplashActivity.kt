package com.app.cityshow.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.LocalDataHelper

class SplashActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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