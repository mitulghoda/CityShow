package com.app.cityshow.ui.common

import android.content.Intent
import com.app.cityshow.ui.activity.HomeActivity

abstract class NavigationActivity : BaseActivity() {

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finishAffinity()
    }

}