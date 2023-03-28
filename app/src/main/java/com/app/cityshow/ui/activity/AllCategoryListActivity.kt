package com.app.cityshow.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityProductListBinding
import com.app.cityshow.model.category.Category
import com.app.cityshow.ui.adapter.CategoryListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel

class AllCategoryListActivity : ActionBarActivity() {
    private lateinit var category: Category
    lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel
    override fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        setUpToolbar(getString(R.string.all_categories), true)
        val layoutManager = GridLayoutManager(this, 3)
        binding.laySearch.recyclerView.layoutManager = layoutManager
        categoryListAdapter = CategoryListAdapter(arrayListOf()) {
            openProductListActivity(it)
        }
        binding.laySearch.recyclerView.adapter = categoryListAdapter
        binding.laySearch.root.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callGetCategoryApi()
    }

    private fun callGetCategoryApi() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        param["page"] = "1"
        param["limit"] = "10000"
        viewModel.getCategories(param).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                if (it.data != null && it.data.success) {
                    categoryListAdapter.setData(it.data.data.categories)
                } else {
                    showAlertMessage(it.message)
                }
            }, error = {
                hideProgressDialog()
                showAlertMessage(it.message)
            }, loading = {
                showProgressDialog()
            })
        }
    }
}