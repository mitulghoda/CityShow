package com.app.cityshow.ui.common

import android.content.Intent
import com.app.cityshow.BuildConfig
import com.app.cityshow.ui.activity.AddProductActivity
import com.app.cityshow.ui.activity.EditProfileActivity
import com.app.cityshow.ui.activity.HomeActivity
import com.app.cityshow.ui.activity.ProductDetailsActivity
import com.filepickersample.bottomsheet.AndroidFilePicker
import com.filepickersample.enumeration.FileSelectionType
import com.filepickersample.listener.FilePickerCallback

abstract class NavigationActivity : BaseActivity() {

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

    fun openEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    fun openProductDetails() {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        startActivity(intent)
    }

    fun openImageFilePicker(callback: FilePickerCallback) {
        AndroidFilePicker.with(BuildConfig.APPLICATION_ID)
            .type(FileSelectionType.IMAGE)
            .enableMultiSelection()
            .callBack(callback)
            .start(supportFragmentManager)
    }
}