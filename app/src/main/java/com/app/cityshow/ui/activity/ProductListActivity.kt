package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.category.Category
import com.app.cityshow.model.product.Product
import com.app.cityshow.pagination.GridPaginationHelper
import com.app.cityshow.pagination.GridPaginationHelper.Companion.PAGE_SIZE
import com.app.cityshow.pagination.PaginationHelper
import com.app.cityshow.ui.adapter.ProductAdapterWithPagination
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel


class ProductListActivity : ActionBarActivity() {
    private lateinit var strSearchText: String
    private lateinit var category: Category
    lateinit var productListAdapter: ProductAdapterWithPagination
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel
    private var searchText: String? = null
    private var paginationHelper: GridPaginationHelper<Product>? = null
    val productList = ArrayList<Product>()
    override fun initUi() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("CATEGORY_ID")) {
            category = intent.getSerializableExtra("CATEGORY_ID") as Category
            setUpToolbar(category.name, true, imgOption = true)
        }


        val layoutManager = GridLayoutManager(this, 2)
        binding.laySearch.recyclerView.layoutManager = layoutManager

        productListAdapter = ProductAdapterWithPagination(this, productList) { product, type ->
            when (type) {
                0 -> {
                    markFavProduct(product)
                }
                1 -> {
                    openProductDetails(product)
                }
            }

        }
        binding.laySearch.recyclerView.adapter = productListAdapter
        binding.laySearch.root.show()
        paginationHelper = GridPaginationHelper(this,
            binding.laySearch.layError,
            binding.laySearch.recyclerView,
            layoutManager,
            binding.laySearch.progressBar,
            paginationCallback = object : GridPaginationHelper.PaginationCallback {
                override fun onNewPage(pageNumber: Int) {
                    onNewPageCall(pageNumber)
                }
            })
        paginationHelper?.resetValues()
        onNewPageCall(PaginationHelper.START_PAGE_INDEX)
    }

    private fun onNewPageCall(pageNumber: Int) {
        paginationHelper?.handleErrorView(View.GONE, "", View.GONE, View.GONE)
        paginationHelper?.setProgressLayout(View.VISIBLE)
        binding.root.postDelayed({
            calGetProducts(searchText, pageNumber, category.id)
        }, 300)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun calGetProducts(searchText: String? = null, pageNumber: Int, strId: String) {
        val param = HashMap<String, Any>()
        param["category_id"] = strId
        if (searchText != null) {
            param["searchText"] = searchText.toString()
        }
        param["latitude"] = lattitude.toString()
        param["longitude"] = longitude.toString()
        param["pagination"] = "true"
        param["limit"] = PAGE_SIZE
        param["page"] = pageNumber
        viewModel.listOfProduct(param).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                val data = it.data
                if (data != null && data.data.products.isNotEmpty()) {
                    binding.laySearch.recyclerView.show()
                    productList.addAll(data.data.products)
                    paginationHelper?.setSuccessResponse(
                        data.success, data.data.products, data.message
                    )
                } else paginationHelper?.setFailureResponse(getString(R.string.no_product_found_category))
            }, error = {
                hideProgressDialog()
                paginationHelper?.setFailureResponse(getString(R.string.no_product_found_category))
            }, loading = {
                if (pageNumber == 1) {
                    showProgressDialog()
                }
                paginationHelper?.handleErrorView(View.GONE, "", View.GONE, View.GONE)
                paginationHelper?.setProgressLayout(View.VISIBLE)
            })

        }

    }

    private fun markFavProduct(device: Product) {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["product_id"] = device.id ?: ""
        viewModel.markFav(param).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                if (it.data != null && it.data.success) {
                    toast(it.data.message)
                } else {
                    showAlertMessage(
                        "", it.data?.message ?: getString(R.string.something_went_wrong)
                    )
                }
            }, error = {
                hideProgressDialog()
                showAlertMessage("", it.message)
            }, loading = { showProgressDialog() })
        }

    }

    override fun onSearchTextChanged(newText: String) {
        super.onSearchTextChanged(newText)
        strSearchText = newText
        handleClickEventsDebounced(Unit)
    }

    val handleClickEventsDebounced = debounce<Unit>(500, lifecycleScope) {
        paginationHelper?.resetValues()
        calGetProducts(strSearchText, pageNumber = 1, category.id)
    }

}