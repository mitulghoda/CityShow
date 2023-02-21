package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.MyProductListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import java.util.ArrayList

class MyProductListActivity : ActionBarActivity(), View.OnClickListener {
    lateinit var productListAdapter: MyProductListAdapter
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel

    override fun initUi() {
        setUpToolbar(getString(R.string.my_product), true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        binding.clickListener = this
        binding.fab.show()
        setAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        callGetMyProductList()
    }

    private fun setAdapter() {
        productListAdapter =
            MyProductListAdapter(arrayListOf()) { product: Product, type: Int, pos: Int ->
                when (type) {
                    0 -> {
                        showAlertMessage(
                            getString(R.string.delete),
                            getString(R.string.are_you_sure_want_to_delete)
                        ) {
                            if (it) {
                                deleteProduct(product)
                                productListAdapter.mArrayList.remove(product)
                                productListAdapter.notifyItemRemoved(pos)
                            }
                        }
                    }
                    1 -> {
                        openAddProductActivity(product)
                    }
                }
            }
        binding.laySearch.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.laySearch.recyclerView.adapter = productListAdapter

    }

    private fun deleteProduct(device: Product) {
        showProgressDialog()
        viewModel.deleteProduct(device.id ?: "").observe(this) {
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

    private fun callGetMyProductList() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["user_id"] = LocalDataHelper.userId
        viewModel.myProduct(param).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.products
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

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fab -> {
                openAddProductActivity()
            }

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