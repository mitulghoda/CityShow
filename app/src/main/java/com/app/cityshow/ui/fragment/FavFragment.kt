package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.databinding.FavListBinding
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

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
        val mArrayList = ArrayList<CategoryModel>()
        productListAdapter = ProductListAdapter(arrayListOf()) {

        }
        binding.rvProducts.adapter = productListAdapter
    }

    private fun calGetProducts() {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        viewModel.listOfProduct(param).observe(viewLifecycleOwner) {
            base?.hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        Log.e("Products", Gson().toJson(it.data.data.products))
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                },
                error = {
                    base?.showAlertMessage(it.message)
                }, loading = { base?.showProgressDialog() })
        }

    }
}