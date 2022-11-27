package com.app.cityshow.ui.bottomsheet

import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.View
import com.app.cityshow.databinding.LayoutBottomsheetHeaderBinding

abstract class BaseHeaderBottomSheet : BaseBottomSheet(), OnShowListener, View.OnClickListener {
    lateinit var headerView: LayoutBottomsheetHeaderBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerView.imgClose.setOnClickListener(this)
    }

    protected fun changeTitle(title: String) {
        headerView.txtTitle.text = title
    }

    override fun onClick(view: View?) {
        when (view) {
            headerView.imgClose -> dismiss()
        }
    }
}