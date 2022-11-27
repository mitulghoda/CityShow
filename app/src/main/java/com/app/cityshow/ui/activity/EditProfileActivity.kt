package com.app.cityshow.ui.activity

import android.os.Bundle
import com.app.cityshow.databinding.ActivityEditProfileBinding
import com.app.cityshow.databinding.ActivityShopsBinding
import com.app.cityshow.ui.common.ActionBarActivity

class EditProfileActivity : ActionBarActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolbar("Update Profile", true)
        setSubTitleText("Ahmedabad")
    }
}