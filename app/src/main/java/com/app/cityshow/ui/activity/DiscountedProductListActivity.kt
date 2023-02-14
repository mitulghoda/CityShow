package com.app.cityshow.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import java.util.ArrayList

class DiscountedProductListActivity : ActionBarActivity() {
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel

    override fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        setAdapter()
        if (intent.hasExtra("DISCOUNT_ID")) {
            val discount = intent.getSerializableExtra("DISCOUNT_ID") as Discount
            setUpToolbar(getString(R.string.discounted_products), true)
            setSubTitleText(discount.coupon_name)
            calGetProducts(discount.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAdapter() {
        productListAdapter = ProductListAdapter(arrayListOf()) { product: Product, type: Int ->
        }
        binding.laySearch.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.laySearch.recyclerView.adapter = productListAdapter
    }

    private fun calGetProducts(strId: String) {
        showProgressDialog()
        viewModel.getDiscountedProduct(strId).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.discount_products as ArrayList<Product>
                        setData(list)
                    } else {
                        updateView(true)
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    updateView(true)
                    showAlertMessage(it.message)
                }, loading = { showProgressDialog() })
        }

    }

    private fun setData(list: ArrayList<Product>) {
        if (list.isEmpty()) {
            updateView(true)
        } else {
            updateView(false)
            productListAdapter.setData(list)
        }
    }

    private fun updateView(isShowError: Boolean) {
        if (isShowError) {
            binding.laySearch.layError.root.show()
            binding.laySearch.layError.txtErrorMsg.text =
                getString(R.string.no_data_found)
        } else {
            binding.laySearch.layError.root.hide()
        }

    }
}