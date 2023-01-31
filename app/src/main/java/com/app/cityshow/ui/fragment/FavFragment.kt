package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.FavListBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.FavProductListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.utility.visible
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson
import java.util.ArrayList

class FavFragment : BaseFragment() {
    lateinit var productListAdapter: FavProductListAdapter
    private lateinit var binding: FavListBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FavListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        calGetProducts()
        setAdapter()
    }

    private fun setAdapter() {
        productListAdapter = FavProductListAdapter(arrayListOf()) { product: Product, type: Int ->
            when (type) {
                0 -> {
                    markFavProduct(product)
                }
                1 -> {
                    navigation?.openProductDetails(product)
                }
            }
        }
        binding.laySearch.recyclerView.adapter = productListAdapter
        binding.laySearch.recyclerView.layoutManager = GridLayoutManager(activity, 2)
    }

    private fun markFavProduct(device: Product) {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["product_id"] = device.id ?: ""
        viewModel.markFav(param).observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        base?.toast(it.data.message)
                    } else {
                        base?.showAlertMessage(
                            "",
                            it.data?.message ?: getString(R.string.something_went_wrong)
                        )
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage("", it.message)
                }, loading = {})
        }

    }

    private fun calGetProducts() {
        base?.showProgressDialog()
        viewModel.getFavProduct().observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        Log.e("FavProducts", Gson().toJson(it.data.data.products))
                        val list = it.data.data.products
                        setData(list)

                    } else {
                        base?.showAlertMessage(it.message)
                    }
                },
                error = {
                    base?.showAlertMessage(it.message)
                }, loading = { base?.showProgressDialog() })
        }

    }

    private fun setData(list: ArrayList<Product>) {
        if (list.isEmpty()) {
            binding.laySearch.layError.root.visible()
        } else {
            productListAdapter.setData(list)
        }

    }
}