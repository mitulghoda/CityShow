package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.category.Category
import com.app.cityshow.model.product.Product
import com.app.cityshow.pagination.PaginationHelper
import com.app.cityshow.ui.adapter.SearchFriendAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel

class ProductListActivity : ActionBarActivity() {
    private lateinit var category: Category
    lateinit var productListAdapter: SearchFriendAdapter
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel
    private var searchText: String? = null
    private var paginationHelper: PaginationHelper<Product>? = null
    val productList = ArrayList<Product>()
    override fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("CATEGORY_ID")) {
            category = intent.getSerializableExtra("CATEGORY_ID") as Category
            setUpToolbar(category.name, true, imgOption = true)
        }
        val layoutManager = GridLayoutManager(this, 2)
        binding.laySearch.recyclerView.layoutManager = layoutManager
        productListAdapter = SearchFriendAdapter(this, productList) { product, type ->
            openProductDetails(product)
        }
        binding.laySearch.recyclerView.adapter = productListAdapter
        binding.laySearch.root.show()
        paginationHelper = PaginationHelper(
            binding.laySearch.recyclerView,
            layoutManager,
            binding.laySearch.layError,
            binding.laySearch.progressBar,
            this::onNewPageCall
        )
        paginationHelper?.refreshDataFromFirstPage()
    }

    private fun onNewPageCall(pageNumber: Int) {
        calGetProducts(searchText, pageNumber, category.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun calGetProducts(searchText: String? = null, pageNumber: Int, strId: String) {
        val param = HashMap<String, Any>()
        param["category_id"] = strId
        searchText.let { param["searchText"] = searchText.toString() }
        param["latitude"] = lattitude.toString()
        param["longitude"] = longitude.toString()
        param["pagination"] = "false"
        param["limit"] = "3"
        param["page"] = pageNumber
        viewModel.listOfProduct(param).observe(this) {
            it.status.typeCall(success = {
                val data = it.data
                if (data != null) {
                    binding.laySearch.recyclerView.show()
                    productList.addAll(data.data.products)
                    paginationHelper?.setSuccessResponse(
                        data.success,
                        data.data.products,
                        data.message
                    )
                } else paginationHelper?.setFailureResponse(it.message)
            }, error = {
                paginationHelper?.setFailureResponse(it.message)
            }, loading = {
                paginationHelper?.handleErrorView("", View.GONE)
                paginationHelper?.setProgressLayout(View.VISIBLE)
            })

        }

    }

    override fun onSearchTextChanged(newText: String) {
        super.onSearchTextChanged(newText)
        calGetProducts(newText, pageNumber = 1, category.id)
    }
}