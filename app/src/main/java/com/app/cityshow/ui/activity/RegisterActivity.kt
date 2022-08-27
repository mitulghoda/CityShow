package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.cityshow.R
import com.app.cityshow.databinding.LoginBinding
import com.app.cityshow.databinding.RegisterBinding
import com.app.cityshow.ui.common.NavigationActivity

class RegisterActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: RegisterBinding
    override fun initUi() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSubmit -> {
                openOTPActivity()
            }
            binding.ivBack -> {
                finish()
            }

        }

    }
}