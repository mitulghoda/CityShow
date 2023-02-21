package com.app.cityshow.ui.common

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.cityshow.databinding.LayoutToolbarBinding
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class ActionBarActivity : NavigationActivity(), View.OnClickListener {
    lateinit var actionView: LayoutToolbarBinding

    override fun setContentView(view: View?) {
        if (view != null) {
            actionView = LayoutToolbarBinding.bind(view)
            setSupportActionBar(actionView.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            clickListeners()
        }
        super.setContentView(view)
    }

    private fun clickListeners() {
    }

    private fun homeUpEnable(enable: Boolean) {
        actionView.imgBack.visibility = View.VISIBLE.takeIf { enable } ?: View.GONE
        actionView.imgLogo.visibility = View.VISIBLE.takeIf { !enable } ?: View.GONE
        lifecycleScope.launch {
            delay(200)
            setSubTitleText(getAddress(LatLng(lattitude, longitude)))
        }
    }

    override fun setAddress() {
        super.setAddress()
        lifecycleScope.launch {
            delay(200)
            setSubTitleText(getAddress(LatLng(lattitude, longitude)))
        }
    }

    protected fun setUpToolbar(
        title: String?,
        isHomeUpEnabled: Boolean = true,
        isFilterVisible: Boolean = false,
    ) {
        actionView.txtToolbarTitle.text = title
        homeUpEnable(isHomeUpEnabled)
        actionView.ivFilter.visibility = View.VISIBLE.takeIf { isFilterVisible } ?: View.GONE
    }

    protected fun setUpToolbar(resId: Int, isHomeUpEnabled: Boolean? = true) {
        setUpToolbar(getString(resId), isHomeUpEnabled!!)
    }

    protected fun changeTitle(string: String) {
        actionView.txtToolbarTitle.text = string
    }

    fun setSubTitleText(value: String?) {
        actionView.txtSubTitle.text = value
        actionView.txtSubTitle.visibility =
            View.GONE.takeIf { value == null || value.isEmpty() } ?: View.VISIBLE
    }

    fun setSubTitleTextColor(resId: Int) {
        actionView.txtSubTitle.setTextColor(ContextCompat.getColor(this, resId))
    }

    fun setSubTitleText(value: String, count: Long) {
        actionView.txtSubTitle.text = value
        actionView.txtSubTitle.visibility = View.GONE.takeIf { count <= 0 } ?: View.VISIBLE
    }

    fun setSubTitleEnable(value: Boolean) {
        actionView.txtSubTitle.visibility = View.VISIBLE.takeIf { value } ?: View.GONE
    }

    override fun onClick(view: View?) {
        when (view) {
            actionView.imgBack -> onBackPressed()
            actionView.imgOption -> onOptionMenuClick()
            actionView.ivFilter -> onFilterClick()
        }
    }

    override fun onBackPressed() {
        hideKeyBoard()
        super.onBackPressed()
    }

    // click methods
    open fun onBluetooothMenuClick() {}
    open fun onOptionMenuClick() {}
    open fun onActionMenuDoneClick() {}
    open fun onFilterClick() {}
    open fun onProfileClick() {}
}