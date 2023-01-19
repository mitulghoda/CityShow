package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.R
import com.app.cityshow.databinding.FrgProductDetailsBinding
import com.app.cityshow.ui.common.BaseFragment

class ProductDetailsFragment(val imageUrl: String) : BaseFragment() {
    private var binding: FrgProductDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FrgProductDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.strUrl = imageUrl
//        binding?.imageView?.setIma(R.drawable.dummy_mobile)
    }
}