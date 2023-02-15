package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityShopsBinding
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.adapter.ShopsAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
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
        binding.clickListener = this
        initViewModel()
        shopsAdapter = ShopsAdapter(arrayListOf()) {
            openShopDetails(it)
        }
        binding.laySearch.recyclerView.adapter = shopsAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetMyShop()
    }

    private fun callGetMyShop() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["page"] = "1"
        param["limit"] = "100"
        param["pagination"] = "true"
        viewModel?.myShops(param)?.observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        Log.e("CATEGORIES", Gson().toJson(it.data.data))
                        setData(it.data.data.shops)
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    showAlertMessage(it.message)
                }, loading = {
                    showProgressDialog()
                })
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun setData(shops: List<Shop>) {
        if (shops.isEmpty()) {
            binding.laySearch.layError.root.show()
            binding.laySearch.layError.txtErrorMsg.text = getString(R.string.no_shop_found)
        } else {
            shopsAdapter?.setData(shops)
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fab -> {
                openAddShopActivity(null)
            }

        }
    }
}