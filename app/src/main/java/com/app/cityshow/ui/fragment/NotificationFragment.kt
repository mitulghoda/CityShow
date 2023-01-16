package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.databinding.NotificationsListBinding
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.ui.adapter.NotificationAdapter
import com.app.cityshow.ui.common.BaseFragment

class NotificationFragment : BaseFragment() {
    lateinit var notificationAdapter: NotificationAdapter
    private lateinit var binding: NotificationsListBinding
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
        setAdapter()
    }

    private fun setAdapter() {
        val mArrayList = ArrayList<CategoryModel>()
        notificationAdapter = NotificationAdapter(mArrayList) {

        }
        binding.rvProducts.adapter = notificationAdapter
    }
}