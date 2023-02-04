package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.HomeFragmentBinding
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.CategoryListAdapter
import com.app.cityshow.ui.adapter.DiscountListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class HomeFragment : BaseFragment() {
    lateinit var categoryListAdapter: CategoryListAdapter
    lateinit var discountsAdapter: DiscountListAdapter
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: HomeFragmentBinding
    private var viewModel: ProductViewModel? = null
    val mArrayList = ArrayList<CategoryModel>()
    val productList = ArrayList<Product>()
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
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetCategoryApi()
        calGetProducts()
        callGetMyDiscounts()
    }

    private fun callGetMyDiscounts() {
        val param = HashMap<String, Any>()
        viewModel?.myDiscounts(param)?.observe(this) {
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        Log.e("disocunts", Gson().toJson(it.data.data))
                        discountsAdapter.setData(it.data.data.discounts)
                    } else {
                    }
                },
                error = {
                }, loading = {
                })
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun callGetCategoryApi() {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "true"
        param["page"] = "1"
        param["limit"] = "10000"
        viewModel?.getCategories(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        Log.e("CATEGORIES", Gson().toJson(it.data.data))
                        categoryListAdapter.setData(it.data.data.categories)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage(it.message)
                }, loading = {})
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun calGetProducts() {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["page"] = "1"
        param["limit"] = "100"
        param["pagination"] = "true"
        viewModel?.listOfProduct(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        Log.e("Products", Gson().toJson(it.data.data.products))
                        val list = it.data.data.products as java.util.ArrayList
                        productListAdapter.setData(list)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage(it.message)
                }, loading = { base?.showProgressDialog() })
        }

    }

    private fun setAdapter() {
        categoryListAdapter = CategoryListAdapter(arrayListOf()) {
            navigation?.openProductListActivity(it)
        }
        discountsAdapter = DiscountListAdapter(arrayListOf()) {}
        binding.rvCategories.adapter = categoryListAdapter
        binding.rvDiscounts.adapter = discountsAdapter

        productListAdapter = ProductListAdapter(productList) { product, type ->
            when (type) {
                0 -> {
                    markFavProduct(product)
                }
                1 -> {
                    navigation?.openProductDetails(product)
                }
            }

        }
        binding.rvProducts.adapter = productListAdapter
    }

    private fun markFavProduct(device: Product) {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["product_id"] = device.id ?: ""
        viewModel?.markFav(param)?.observe(viewLifecycleOwner) {
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
}