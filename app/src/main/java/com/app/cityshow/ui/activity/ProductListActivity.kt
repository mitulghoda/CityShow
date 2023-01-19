package com.app.cityshow.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class ProductListActivity : ActionBarActivity() {
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel


    override fun initUi() {

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("CATEGORY_ID")) {
            calGetProducts(intent.getStringExtra("CATEGORY_ID")!!)
        }
        setAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar("Home", true)
        setSubTitleText("Ahmedabad")
    }

    private fun setAdapter() {
        productListAdapter = ProductListAdapter(arrayListOf()) { product: Product, type: Int ->

        }
        binding.rvProducts.adapter = productListAdapter
    }

    private fun calGetProducts(strId: String) {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["category_id"] = strId
        param["pagination"] = "false"
        viewModel.listOfProduct(param).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.products
                        productListAdapter.setData(list)

                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    showAlertMessage(it.message)
                }, loading = { showProgressDialog() })
        }

    }
}