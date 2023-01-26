package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.BuildConfig
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ProfileBinding
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.loadImage
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.UserViewModel

class ProfileFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: ProfileBinding
    private lateinit var viewModel: UserViewModel

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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]
        setUserData()
    }

    /**
     * Set Default User data
     */

    private fun setUserData() {
        val user = LocalDataHelper.user
        binding.txtName.text = String.format("${user?.firstName} ${user?.lastname}")
        binding.txtStatus.text = user?.status
        binding.imgProfile.loadImage(user?.full_profile_image, R.drawable.ic_user)

        binding.txtVersion.text = BuildConfig.VERSION_NAME
    }

    override fun onClick(p0: View?) {

        when (p0) {
            binding.layShops -> {
                navigation?.openShopsActivity()
            }
            binding.layProducts -> {
                navigation?.openMyProductListActivity()
            }
            binding.layDiscount -> {
                navigation?.openDiscountActivity()
            }
            binding.linearLayoutProfile -> {
                navigation?.openEditProfileActivity {
                    binding.imgProfile.loadImage(
                        LocalDataHelper.user?.full_profile_image,
                        R.drawable.ic_user
                    )
                }
            }
            binding.txtLogout -> {
                base?.showAlertMessage(
                    title = "Logout!",
                    strMessage = "Are you sure? You want to logout?",
                    isCancelable = false,
                    positiveText = "Logout",
                    negativeText = "Cancel"
                ) {
                    if (it) {
                        logout()
                    }
                }
            }
        }
    }

    private fun logout() {
        base?.showProgressDialog()
        viewModel.logout().observe(this) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    base?.logoutActions()
                },
                error = {
                    base?.hideProgressDialog()
                    base?.logoutActions()
                }, loading = {})
        }
    }


}