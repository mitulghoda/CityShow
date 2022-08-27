package com.app.cityshow.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.cityshow.ui.common.BaseFragment

class PageStateAdapter<E : BaseFragment?> : FragmentStateAdapter {
    val items: List<E?>?
    private var titles: List<String>? = null

    constructor(
        fragmentActivity: FragmentActivity,
        fragments: List<E?>?
    ) : super(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle) {
        items = fragments
    }

    constructor(
        fragmentActivity: FragmentActivity?,
        titles: List<String>?,
        fragments: List<E>?
    ) : super(
        fragmentActivity!!
    ) {
        items = fragments
        this.titles = titles
    }

    fun getItem(index: Int): E? {
        if (items == null || items.size <= 0) return null
        if (index < 0) return null
        return if (index >= items.size) null else items[index]
    }

    override fun createFragment(position: Int): Fragment {
        return items!![position]!!
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    fun getTitle(position: Int): String {
        if (titles == null || titles!!.size <= 0) return ""
        return if (position < 0 || position >= titles!!.size) "" else titles!![position]
    }
}