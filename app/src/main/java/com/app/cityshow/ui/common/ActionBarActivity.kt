package com.app.cityshow.ui.common

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.cityshow.databinding.LayoutToolbarBinding
import com.app.cityshow.utility.show
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.gms.maps.model.LatLng
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
        actionView.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                onSearchTextChanged(newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
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
        imgOption: Boolean = false,
    ) {
        actionView.txtToolbarTitle.text = title
        homeUpEnable(isHomeUpEnabled)
        actionView.ivFilter.visibility = View.VISIBLE.takeIf { isFilterVisible } ?: View.GONE
        actionView.searchView.visibility = View.VISIBLE.takeIf { imgOption } ?: View.GONE
    }

    protected fun setUpToolbar(resId: Int, isHomeUpEnabled: Boolean? = true) {
        setUpToolbar(getString(resId), isHomeUpEnabled!!)
    }

    protected fun changeTitle(string: String) {
        actionView.txtToolbarTitle.text = string
    }

    private fun setSubTitleText(value: String?) {
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
            actionView.imgOption -> {
                actionView.searchView.show()
            }
            actionView.ivFilter -> onFilterClick()
        }
    }

    override fun onBackPressed() {
        hideKeyBoard()
        actionView.searchView.closeSearch(true)
        super.onBackPressed()
    }

    // click methods
    open fun onSearchTextChanged(newText: String) {}
    open fun onActionMenuDoneClick() {}
    open fun onFilterClick() {}
    open fun onProfileClick() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (actionView.searchView.onActivityResult(requestCode, resultCode, data)) {
                return
            }
        }
    }
}