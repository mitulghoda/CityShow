package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.databinding.HomeFragmentBinding
import com.app.cityshow.model.CategoryModel
import com.app.cityshow.ui.adapter.CategoryListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment

class HomeFragment : BaseFragment() {
    lateinit var categoryListAdapter: CategoryListAdapter
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: HomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        val mArrayList = ArrayList<CategoryModel>()
        categoryListAdapter = CategoryListAdapter(mArrayList, {

        })
        binding.recyclerView.adapter = categoryListAdapter

        productListAdapter = ProductListAdapter(mArrayList, {

        })
        binding.rvProducts.adapter = productListAdapter
    }
}