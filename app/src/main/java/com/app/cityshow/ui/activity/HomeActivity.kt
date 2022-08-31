package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import com.app.cityshow.R
import com.app.cityshow.databinding.HomeBinding
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.fragment.HomeFragment
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.fragment.FavFragment
import com.app.cityshow.ui.fragment.NotificationFragment
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
import java.util.ArrayList

class HomeActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: HomeBinding
    private var pageStateAdapter: PageStateAdapter<BaseFragment>? = null
    override fun initUi() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = HomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Home", false)
        setSubTitleText("Ahmedabad")
        setupViewPagerFragment()
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return
        val fragments: MutableList<BaseFragment?> = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(FavFragment())
        fragments.add(NotificationFragment())
        fragments.add(HomeFragment())
        mBinding.viewPager.adapter =
            PageStateAdapter(this, fragments).also { pageStateAdapter = it }
        mBinding.viewPager.isUserInputEnabled = false
        mBinding.bottomView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    setUpToolbar("Home", false)
                    mBinding.viewPager.currentItem = 0
                }
                R.id.action_fav -> {
                    setUpToolbar("Wishlist", false)
                    mBinding.viewPager.currentItem = 1
                }
                R.id.action_not -> {
                    setUpToolbar("Notifications", false)
                    mBinding.viewPager.currentItem = 2
                }
                R.id.action_account -> {
                    setUpToolbar("Account", false)
                    mBinding.viewPager.currentItem = 3
                }
            }
            false
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.fab -> {
                openAddProductActivity()
            }

        }
    }
}