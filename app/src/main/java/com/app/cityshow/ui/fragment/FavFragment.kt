package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.databinding.FavListBinding
import com.app.cityshow.model.CategoryModel
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment

class FavFragment : BaseFragment() {
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: FavListBinding
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
        setAdapter()
    }

    private fun setAdapter() {
        val mArrayList = ArrayList<CategoryModel>()
        productListAdapter = ProductListAdapter(mArrayList) {

        }
        binding.rvProducts.adapter = productListAdapter
    }
}