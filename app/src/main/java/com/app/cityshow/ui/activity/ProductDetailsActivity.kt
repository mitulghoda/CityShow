package com.app.cityshow.ui.activity

import android.content.Intent
import android.net.Uri
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
import com.stfalcon.imageviewer.StfalconImageViewer

class ProductDetailsActivity : ActionBarActivity(), View.OnClickListener {
    private var productdata: Product? = null
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
            mBinding.tvCall -> {
                justTry {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + "${productdata?.shopkeeper?.phoneNumber}")
                    startActivity(intent)
                }
            }
            mBinding.tvMore -> {
                BottomSheetMoreDetails.newInstance(mBinding.productData).show(this)
            }
            mBinding.ivShare -> {
               appSharing(this,getString(R.string.app_name),"You can visit ${productdata?.shop?.shop_name}")
            }
            mBinding.ivMap -> {
                justTry {
                    val shop = productdata?.product_shop?.get(0)
                    if (shop?.latitude.isNullOrEmpty() || shop?.longitude.isNullOrEmpty()) {
                        navigateMap(
                            lattitude,
                            longitude
                        )
                    } else {
                        navigateMap(
                            shop?.latitude?.toDouble() ?: lattitude,
                            shop?.longitude?.toDoubleOrNull() ?: longitude
                        )
                    }
                }
            }
            mBinding.viewPager -> {
                val images = ArrayList<String>()
                productdata?.product_image?.forEach { myItemImages ->
                    images.add(myItemImages.image_url)
                }
                StfalconImageViewer.Builder(this, images) { view, image ->
                }.withStartPosition(0).show()
            }
            /* mBinding.ivShop -> {
                 if (productdata?.product_shop.isNullOrEmpty()) return
                 StfalconImageViewer.Builder(
                     this,
                     arrayListOf(productdata?.getShopImage())
                 ) { _, _ ->
                 }.show()
             }*/
        }
    }

    private fun setupViewPagerFragment() {
        if (pageStateAdapter != null) return
        mBinding.viewPager.setPageTransformer(DepthPageTransformer())
        mBinding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

        })
    }

    private fun getProductDetails(id: String) {
        showProgressDialog()
        viewModel.getProductDetails(id).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        mBinding.layMain.show()
                        productdata = it.data.data
                        getFragments(it.data.data)
                        mBinding.productData = it.data.data
                        mBinding.moreDetail.productData = it.data.data
                        mBinding.ivShop.loadImage(it.data.data.getShopImage())
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = { showProgressDialog() })
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun getFragments(data: Product?) {
        val fragments: MutableList<BaseFragment> = ArrayList()
        data?.product_image?.forEach { images ->
            fragments.add(
                ProductDetailsFragment.newInstance(images.image_url) {
                    val images = ArrayList<String>()
                    productdata?.product_image?.forEach { myItemImages ->
                        images.add(myItemImages.image_url)
                    }
                    StfalconImageViewer.Builder(this, images) { view, image ->
                        view.loadImage(image)
                    }.withStartPosition(0).show()
                }
            )
        }
        mBinding.viewPager.adapter = PageStateAdapter(this, fragments).also {
            pageStateAdapter = it
        }
        mBinding.pageIndicator.setupWithViewPager(mBinding.viewPager)
    }
}