package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ShopDetailsBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.adapter.PageStateAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.ui.fragment.ProductDetailsFragment
import com.app.cityshow.utility.DepthPageTransformer
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import kotlin.collections.HashMap
import kotlin.collections.set

class ShopDetailsActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: ShopDetailsBinding
    private lateinit var viewModel: ProductViewModel

    var pageStateAdapter: PageStateAdapter<BaseFragment>? = null
    var productListAdapter: ProductListAdapter? = null
    var shop: Shop? = null

    override fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("SHOP")) {
            shop = intent.getSerializableExtra("SHOP") as Shop
//            getProductDetails(product.id.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ShopDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        mBinding.data = shop
        setUpToolbar("Details", true)
        setAdapter()
        setupViewPagerFragment()
        getFragments(shop)

        actionView.imgEdit.visibility = View.VISIBLE
    }

    private fun setAdapter() {
        productListAdapter = shop?.products?.let {
            ProductListAdapter(it) { product, type ->
                when (type) {
                    0 -> {
                        markFavProduct(product)
                    }
                    1 -> {
                        openProductDetails(product)
                    }
                }

            }
        }
        mBinding.rvProducts.adapter = productListAdapter
    }

    private fun markFavProduct(device: Product) {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["product_id"] = device.id ?: ""
        viewModel.markFav(param).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        toast(it.data.message)
                    } else {
                        showAlertMessage(
                            "",
                            it.data?.message ?: getString(R.string.something_went_wrong)
                        )
                    }
                }, error = {
                    hideProgressDialog()
                    showAlertMessage("", it.message)
                }, loading = {})
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            actionView.imgEdit -> {
                openAddShopActivity(shop)
            }
        }
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return

        mBinding.viewPager.setPageTransformer(DepthPageTransformer())
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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

    private fun getFragments(data: Shop?) {
        val fragments: MutableList<BaseFragment> = ArrayList()

        if (data?.shop_images.isNullOrEmpty()) {
            fragments.add(
                ProductDetailsFragment(
                    data?.banner_image.orEmpty()
                )
            )
        } else {
            data?.shop_images?.forEach { images ->
                fragments.add(
                    ProductDetailsFragment(
                        images.image_url
                    )
                )
            }
        }
        mBinding.viewPager.adapter = PageStateAdapter(this, fragments).also {
            pageStateAdapter = it
        }
        mBinding.pageIndicator.setupWithViewPager(mBinding.viewPager)
    }

    private fun getProductDetails(id: String) {
        showProgressDialog()
        viewModel.getProductDetails(id).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
//                        getFragments(it.data.data)
                        mBinding.data = it.data.data.shop

                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = { showProgressDialog() })
        }
    }
}