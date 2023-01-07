package com.app.cityshow.ui.common

import android.content.Intent
import com.app.cityshow.BuildConfig
import com.app.cityshow.ui.activity.*
import com.filepickersample.bottomsheet.AndroidFilePicker
import com.filepickersample.enumeration.FileSelectionType
import com.filepickersample.listener.FilePickerCallback

abstract class NavigationActivity : BaseActivity() {

    fun openOTPActivity() {
        val intent = Intent(this, OTPActivity::class.java)
        startActivity(intent)
    }
    fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finishAffinity()
    }

    fun openAddProductActivity() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }

    fun openShopsActivity() {
        val intent = Intent(this, ShopsActivity::class.java)
        startActivity(intent)
    }

    fun openEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    fun openImageFilePicker(callback: FilePickerCallback) {
        AndroidFilePicker.with(BuildConfig.APPLICATION_ID)
            .type(FileSelectionType.IMAGE)
            .enableMultiSelection()
            .callBack(callback)
            .start(supportFragmentManager)
    }

    fun openProductDetails() {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        startActivity(intent)
    }
}