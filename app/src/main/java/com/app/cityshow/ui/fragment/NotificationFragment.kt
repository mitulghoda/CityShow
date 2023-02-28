package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.cityshow.Controller
import com.app.cityshow.databinding.NotificationsListBinding
import com.app.cityshow.model.Notification
import com.app.cityshow.model.NotificationModel
import com.app.cityshow.ui.adapter.NotificationAdapter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.typeCall
import com.app.cityshow.utility.visible
import com.app.cityshow.viewmodel.ProductViewModel
import com.google.gson.Gson

class NotificationFragment : BaseFragment() {
    lateinit var notificationAdapter: NotificationAdapter
    private lateinit var binding: NotificationsListBinding
    private lateinit var viewModel: ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NotificationsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        setAdapter()
        callGetNotifications()
    }

    private fun setAdapter() {
        notificationAdapter = NotificationAdapter(arrayListOf(), {

        })
        binding.laySearch.recyclerView.adapter = notificationAdapter
        binding.laySearch.recyclerView.layoutManager = LinearLayoutManager(base)
    }


    private fun callGetNotifications() {
        base?.showProgressDialog()
        val params = HashMap<String, Any>()
        params["page"] = "1"
        params["limit"] = "1000"
        params["pagination"] = "false"
        viewModel.getNotifications(params).observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.notification
                        setData(list as ArrayList<Notification>)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                },
                error = {
                    base?.showAlertMessage(it.message)
                }, loading = { base?.showProgressDialog() })
        }

    }

    private fun setData(list: ArrayList<Notification>) {
        if (list.isEmpty()) {
            binding.laySearch.layError.root.visible()
        } else {
            notificationAdapter.setData(list)
        }

    }

}