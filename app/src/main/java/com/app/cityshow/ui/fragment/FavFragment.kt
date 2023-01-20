package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.databinding.FavListBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.utility.visible
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson
import java.util.ArrayList

class FavFragment : BaseFragment() {
    lateinit var productListAdapter: ProductListAdapter
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
        productListAdapter = ProductListAdapter(arrayListOf()) { product: Product, type: Int ->

        }
        binding.laySearch.recyclerView.adapter = productListAdapter
    }

    private fun calGetProducts() {
        base?.showProgressDialog()
        viewModel.getFavProduct().observe(viewLifecycleOwner) {
            base?.hideProgressDialog()
            it.status.typeCall(
                success = {
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