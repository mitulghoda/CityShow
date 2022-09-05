package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import com.app.cityshow.databinding.ProductDetailsBinding
import com.app.cityshow.model.CategoryModel
import com.app.cityshow.ui.adapter.NotificationAdapter
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.common.ActionBarActivity
import java.util.ArrayList

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
        setUpToolbar("Details", false)
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }
}