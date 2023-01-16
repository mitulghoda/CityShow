package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ProductDetailsBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetMoreDetails
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.fragment.ProductDetailsFragment
import com.app.cityshow.utility.*
import com.app.cityshow.viewmodel.ProductViewModel

class ProductDetailsActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: ProductDetailsBinding
    private var pageStateAdapter: PageStateAdapter<BaseFragment>? = null

    private lateinit var viewModel: ProductViewModel

    override fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("PRODUCT")) {
            val product = intent.getSerializableExtra("PRODUCT") as Product
            getProductDetails(product.id.toString())
        }
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
                positionOffsetPixels: Int,
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

    private fun getProductDetails(id: String) {
        showProgressDialog()
        viewModel.getProductDetails(id).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        mBinding.productData = it.data.data
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = { showProgressDialog() })
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
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