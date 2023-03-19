package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityShopsBinding
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.ui.adapter.DiscountsAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class DiscountActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var binding: ActivityShopsBinding
    private var discountsAdapter: DiscountsAdapter? = null

    private var viewModel: ProductViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolbar("My Discounts", true)
        binding.clickListener = this
        initViewModel()
        discountsAdapter = DiscountsAdapter(arrayListOf()) { id, discount, position ->
            when (id) {
                R.id.ivDelete -> {
                    showAlertMessage(
                        getString(R.string.delete),
                        getString(R.string.are_you_sure_want_to_delete)
                    ) {
                        if (it) {
                            deleteDiscount(discount)
                            discountsAdapter?.mArrayList?.remove(discount)
                            discountsAdapter?.notifyItemRemoved(position)
                        }
                    }

                }
                R.id.ivEdit -> {
                    openAddDiscountActivity(discount)
                }
            }
        }
        binding.laySearch.recyclerView.adapter = discountsAdapter
        binding.laySearch.recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun deleteDiscount(discount: Discount) {
        showProgressDialog()
        viewModel?.deleteDiscount(discount.id ?: "")?.observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        toast(it.data.message)
                    } else {
                        showAlertMessage(
                            "",
                            it.data?.message ?: getString(R.string.something_went_wrong)
                        )
                    }
                }, error = {
                    hideProgressDialog()
                    showAlertMessage("", it.message)
                }, loading = {})
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        callGetMyDiscounts()
    }

    private fun callGetMyDiscounts() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["user_id"] = LocalDataHelper.userId
        viewModel?.myDiscounts(param)?.observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        Log.e("CATEGORIES", Gson().toJson(it.data.data))
                        setData(it.data.data.discounts)
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

    private fun setData(shops: List<Discount>) {
        if (shops.isEmpty()) {
            binding.laySearch.layError.root.show()
            binding.laySearch.layError.txtErrorMsg.text = getString(R.string.no_data_found)
        } else {
            discountsAdapter?.setData(shops)
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fab -> {
                openAddDiscountActivity(null)
            }

        }
    }
}