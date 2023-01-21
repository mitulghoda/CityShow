package com.app.cityshow.ui.common

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.app.cityshow.BuildConfig
import com.app.cityshow.model.category.Category
import com.app.cityshow.model.product.Product
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.activity.*
import com.filepickersample.bottomsheet.AndroidFilePicker
import com.filepickersample.enumeration.FileSelectionType
import com.filepickersample.listener.FilePickerCallback

abstract class NavigationActivity : BaseActivity() {

    fun openOTPActivity(email: String) {
        val intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun openAddShopActivity(shop: Shop?) {
        val intent = Intent(this, AddShopActivity::class.java)
        if (shop != null) {
            intent.putExtra("SHOP", shop)
        }
        startActivity(intent)
    }

    fun openForgotPasswordActivity() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    fun openProductListActivity(strId: Category?) {
        val intent = Intent(this, ProductListActivity::class.java)
        intent.putExtra("CATEGORY_ID",strId)
        startActivity(intent)
    }

    fun openChangePasswordActivity(email: String) {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        intent.putExtra("email", email)
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

    fun openEditProfileActivity(callback: (result: ActivityResult) -> Unit) {
        val intent = Intent(this, EditProfileActivity::class.java)
        activityLauncher.launch(intent) { callback.invoke(it) }
    }

    fun openImageFilePicker(callback: FilePickerCallback) {
        AndroidFilePicker.with(BuildConfig.APPLICATION_ID)
            .type(FileSelectionType.IMAGE)
            .enableMultiSelection()
            .callBack(callback)
            .start(supportFragmentManager)
    }

    fun openProductDetails(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }
}