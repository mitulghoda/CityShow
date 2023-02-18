package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.cityshow.R
import com.app.cityshow.databinding.FrgProductDetailsBinding
import com.app.cityshow.ui.common.BaseFragment

class ProductDetailsFragment : BaseFragment() {
    private var binding: FrgProductDetailsBinding? = null
    var imageUrl: String =""
    var listener: (() -> Unit)? = null
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
        binding?.mainLayout?.setOnClickListener {
            listener?.invoke()
        }
    }

    companion object {
        fun newInstance(imageUrl: String, listener: () -> Unit): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            fragment.imageUrl = imageUrl
            fragment.listener = listener
            return fragment
        }
    }
}