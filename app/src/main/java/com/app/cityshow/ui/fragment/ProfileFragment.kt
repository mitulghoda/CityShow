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

class ProfileFragment : BaseFragment(), View.OnClickListener {
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
        initUI()
    }

    private fun initUI() {

        binding.clickListener = this
    }

    override fun onClick(p0: View?) {

        when (p0) {
            binding.layShops -> {
                navigation?.openShopsActivity()
            }
            binding.linearLayoutProfile -> {
                navigation?.openEditProfileActivity()
            }
        }
    }


}