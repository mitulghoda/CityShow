package com.app.cityshow.utility

import android.view.View
import androidx.databinding.BindingAdapter


object BindingAdaptersUtil {
    @JvmStatic
    @BindingAdapter(value = ["android:isVisible"], requireAll = false)
    fun isVisible(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:isGone"], requireAll = false)
    fun isGone(view: View, isGone: Boolean) {
        if (isGone) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:isInvisible"], requireAll = false)
    fun isInvisible(view: View, isInvisible: Boolean) {
        if (isInvisible) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}