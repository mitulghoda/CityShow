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
import com.app.cityshow.ui.fragment.ProfileFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
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
        setUpCurveBottomNavigation()
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return
        val fragments: MutableList<BaseFragment?> = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(FavFragment())
        fragments.add(NotificationFragment())
        fragments.add(ProfileFragment())
        mBinding.viewPager.adapter =
            PageStateAdapter(this, fragments).also { pageStateAdapter = it }
        mBinding.viewPager.isUserInputEnabled = false
        mBinding.bottomView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    showFab(true)
                    setUpToolbar("Home", false)
                    mBinding.viewPager.currentItem = 0
                }
                R.id.action_fav -> {
                    showFab(true)
                    setUpToolbar("Wishlist", false)
                    mBinding.viewPager.currentItem = 1
                }
                R.id.action_not -> {
                    showFab(true)
                    setUpToolbar("Notifications", false)
                    mBinding.viewPager.currentItem = 2
                }
                R.id.action_account -> {
                    showFab(false)
                    setUpToolbar("Account", false)
                    mBinding.viewPager.currentItem = 3
                }
            }
            false
        }
    }

    private fun showFab(isShow: Boolean) {
        mBinding.fab.visibility = View.VISIBLE.takeIf { isShow } ?: View.GONE

    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.fab -> {
                openAddProductActivity()
            }

        }
    }

    private fun setUpCurveBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(1, "", R.drawable.ic_home),
            CurvedBottomNavigation.Model(
                2,
                "",
                R.drawable.ic_fav
            ),
            CurvedBottomNavigation.Model(3,
                "",
                R.drawable.ic_notification),
            CurvedBottomNavigation.Model(4, "", R.drawable.ic_baseline_account_circle_24),
        )
        mBinding.bottomNavigationCurve.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                when (it.id) {
                    1 -> {
                        setUpToolbar("Home", false)
                        mBinding.viewPager.currentItem = 0
                    }
                    2 -> {
                        setUpToolbar("Wishlist", false)
                        mBinding.viewPager.currentItem = 1
                    }
                    3 -> {
                        setUpToolbar("Notifications", false)
                        mBinding.viewPager.currentItem = 2
                    }
                    4 -> {
                        setUpToolbar("Account", false)
                        mBinding.viewPager.currentItem = 3
                    }
                }
            }
        }
        mBinding.bottomNavigationCurve.show(1, true)
    }
}