package com.app.cityshow.ui.activity

import android.os.Bundle
import com.app.cityshow.databinding.ActivityShopsBinding
import com.app.cityshow.ui.adapter.ShopsAdapter
import com.app.cityshow.ui.common.ActionBarActivity

class ShopsActivity : ActionBarActivity() {
    private lateinit var binding: ActivityShopsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolbar("My Shops", true)
        setSubTitleText("Ahmedabad")
        binding.rvItem.adapter = ShopsAdapter(arrayListOf()) {

        }
    }
}