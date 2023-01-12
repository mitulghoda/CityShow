package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.databinding.ActivityShopsBinding
import com.app.cityshow.network.typeCall
import com.app.cityshow.ui.adapter.ShopsAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.Log
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class ShopsActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var binding: ActivityShopsBinding
    private var shopsAdapter: ShopsAdapter? = null

    private var viewModel: ProductViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolbar("My Shops", true)
        setSubTitleText("Ahmedabad")
        binding.clickListener = this
        initViewModel()
        shopsAdapter = ShopsAdapter(arrayListOf()) {
            openAddShopActivity(it)
        }
        binding.rvItem.adapter = shopsAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetCategoryApi()
    }

    private fun callGetCategoryApi() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        viewModel?.myShops(param)?.observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        Log.e("CATEGORIES", Gson().toJson(it.data.data))
                        shopsAdapter?.setData(it.data.data.shops)
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    showAlertMessage(it.message)
                }
            )
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.fab -> {
                openAddShopActivity(null)
            }

        }
    }
}