package com.app.cityshow.ui.activity

import android.os.Bundle
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityPrivacyPolicyBinding
import com.app.cityshow.ui.common.ActionBarActivity

class PrivacyPolicyActivity : ActionBarActivity() {
    private lateinit var mBinding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun initUi() {
        setUpToolbar(getString(R.string.privacy_policy), true)
        mBinding.webView.loadUrl("www.google.com")
    }

}