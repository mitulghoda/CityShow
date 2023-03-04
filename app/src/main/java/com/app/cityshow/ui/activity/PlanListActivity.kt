package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivitySubscritionPlanBinding
import com.app.cityshow.model.subscription.Plan
import com.app.cityshow.ui.adapter.PlanListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.show
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel

class PlanListActivity : ActionBarActivity(), View.OnClickListener {
    lateinit var planListAdapter: PlanListAdapter
    private lateinit var binding: ActivitySubscritionPlanBinding
    private lateinit var viewModel: ProductViewModel

    override fun initUi() {
        setUpToolbar(getString(R.string.subscription), true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        binding.clickListener = this
        setAdapter()
        callPlanListApi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscritionPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAdapter() {
        planListAdapter =
            PlanListAdapter(arrayListOf()) { product: Plan, type: Int ->


            }
        binding.laySearch.recyclerView.adapter = planListAdapter

    }

    private fun callPlanListApi() {
        showProgressDialog()
        viewModel.getSubscriptionsPlans().observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.plans
                        setData(list as ArrayList<Plan>)
                    } else {
                        updateView(true)
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    updateView(true)
                    showAlertMessage(it.message)
                }, loading = { showProgressDialog() })
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {


        }
    }

    private fun setData(list: ArrayList<Plan>) {
        if (list.isEmpty()) {
            updateView(true)
        } else {
            updateView(false)
            planListAdapter.setData(list)
        }
    }

    private fun updateView(isShowError: Boolean) {
        if (isShowError) {
            binding.laySearch.layError.root.show()
            binding.laySearch.layError.txtErrorMsg.text =
                getString(R.string.no_data_found)
        } else {
            binding.laySearch.layError.root.hide()
        }

    }
}