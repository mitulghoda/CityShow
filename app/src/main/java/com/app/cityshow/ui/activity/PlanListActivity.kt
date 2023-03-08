package com.app.cityshow.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivitySubscritionPlanBinding
import com.app.cityshow.model.subscription.Plan
import com.app.cityshow.payment.PaymentSessionHandler
import com.app.cityshow.payment.StripeUtil
import com.app.cityshow.ui.adapter.PlanListAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.Log
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
            PlanListAdapter(arrayListOf()) { plan: Plan, type: Int ->
                selectPayment(plan)

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

    private fun selectPayment(plan: Plan?) {
        if (plan == null) return
        val paymentSessionHandler = StripeUtil.getPaymentSessionHandler(this)
        paymentSessionHandler?.setPackageType("Subscription")
        paymentSessionHandler?.setTitle(plan.name)
        paymentSessionHandler?.setPlan(plan)
        paymentSessionHandler?.description = plan.description
        paymentSessionHandler?.initTransaction((plan.price_data.unit_amount / 100))
        paymentSessionHandler?.setPaymentSessionListener(object :
            PaymentSessionHandler.PaymentSessionListener {
            override fun onPaymentSuccess(payment_intent_id: String?, captured: Boolean) {
                Log.e(
                    "PaymentSessionHandler",
                    "Success : $payment_intent_id Captured - $captured"
                )
                toast("Purchase done")
                finish()
            }

            override fun onPaymentFailed(message: String?) {
                Log.e("PaymentSessionHandler", message ?: "")
                hideProgressDialog()
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        StripeUtil.getPaymentSessionHandler(this)?.onActivityResult(requestCode, resultCode, data)
    }
}