package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.databinding.FavListBinding
import com.app.cityshow.databinding.ProfileBinding
import com.app.cityshow.model.CategoryModel
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.common.BaseFragment

class ProfileFragment : BaseFragment() {
    private lateinit var binding: ProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}