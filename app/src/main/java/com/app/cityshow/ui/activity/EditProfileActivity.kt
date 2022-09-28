package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import com.app.cityshow.databinding.EditProfileBinding
import com.app.cityshow.ui.common.ActionBarActivity

class EditProfileActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: EditProfileBinding
    override fun initUi() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = EditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Edit Profile", true)
    }

    override fun onClick(view: View?) {
        when (view) {
            mBinding.btnSubmit -> {
                finish()
            }
        }
    }
}