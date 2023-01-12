package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.databinding.HomeFragmentBinding
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.network.typeCall
import com.app.cityshow.ui.activity.HomeActivity
import com.app.cityshow.ui.adapter.CategoryListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class HomeFragment : BaseFragment() {
    lateinit var categoryListAdapter: CategoryListAdapter
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: HomeFragmentBinding
    private var viewModel: ProductViewModel? = null
    val mArrayList = ArrayList<CategoryModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setAdapter()

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetCategoryApi()
    }

    private fun callGetCategoryApi() {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        viewModel?.getCategories(param)?.observe(viewLifecycleOwner) {
            base?.hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        Log.e("CATEGORIES", Gson().toJson(it.data.data))
                        categoryListAdapter.setData(it.data.data.categories)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                },
                error = {
                    base?.showAlertMessage(it.message)
                }
            )
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun setAdapter() {
        categoryListAdapter = CategoryListAdapter(arrayListOf()) {

        }
        binding.recyclerView.adapter = categoryListAdapter

        productListAdapter = ProductListAdapter(mArrayList) {
            ((activity as HomeActivity)).openProductDetails()
        }
        binding.rvProducts.adapter = productListAdapter
    }
}