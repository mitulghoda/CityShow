package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.cityshow.R
import com.app.cityshow.databinding.OtpViewBinding
import com.app.cityshow.ui.common.NavigationActivity

class OTPActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var mBinding: OtpViewBinding
    override fun initUi() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = OtpViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
    }

    override fun onClick(p0: View?) {

        when (p0) {
            mBinding.btnVerify -> {
                openHomeActivity()
            }
            mBinding.ivBack -> {
                finish()
            }
        }
    }
}