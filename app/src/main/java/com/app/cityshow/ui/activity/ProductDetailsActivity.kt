package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.cityshow.R
import com.app.cityshow.databinding.ProductDetailsBinding
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetMoreDetails
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.fragment.ProductDetailsFragment
import com.app.cityshow.utility.DepthPageTransformer

class ProductDetailsActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: ProductDetailsBinding
    private var pageStateAdapter: PageStateAdapter<BaseFragment>? = null
    override fun initUi() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ProductDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Details", true)
        setupViewPagerFragment()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            mBinding.tvMore -> {
                BottomSheetMoreDetails().show(this)
            }
        }
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return
        mBinding.viewPager.adapter = PageStateAdapter(this, getFragments()).also {
            pageStateAdapter = it
        }
        mBinding.pageIndicator.setupWithViewPager(mBinding.viewPager)
        mBinding.viewPager.setPageTransformer(DepthPageTransformer())
        mBinding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun getFragments(): List<BaseFragment>? {
        val fragments: MutableList<BaseFragment> = ArrayList()
        fragments.add(
            ProductDetailsFragment(
            )
        )
        fragments.add(
            ProductDetailsFragment(

            )
        )
        fragments.add(
            ProductDetailsFragment(

            )
        )
        fragments.add(
            ProductDetailsFragment(

            )
        )
        return fragments
    }
}