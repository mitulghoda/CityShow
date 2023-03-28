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
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]
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
        binding.txtSubscription.text = user?.subscription?.getPlanName()
        LocalDataHelper.user?.subscription?.stripe_subscription_id?.let {
            binding.txtExpiredDate.text = user?.subscription?.getExpiredDate()
        }
    }

    override fun onClick(p0: View?) {

        when (p0) {
            binding.layShops -> {
                if (checkActiveSubscription()) {
                    navigation?.openShopsActivity()
                }
            }
            binding.layDiscount -> {
                if (checkActiveSubscription()) {
                    navigation?.openDiscountActivity()
                }
            }

            binding.layProducts -> {
                if (checkActiveSubscription()) {
                    navigation?.openMyProductListActivity()
                }
            }

            binding.txtPrivacyPolicy -> {
                navigation?.openPrivacyPolicyActivity("https://cityshow.in/cityshow/public/privacy-policy")
            }
            binding.txtTerms -> {
                navigation?.openPrivacyPolicyActivity("https://cityshow.in/cityshow/public/about-us")
            }
            binding.layPlan -> {
                navigation?.openPlanListActivity()
            }
            binding.linearLayoutProfile -> {
                navigation?.openEditProfileActivity {
                    binding.imgProfile.loadImage(
                        LocalDataHelper.user?.full_profile_image, R.drawable.ic_user
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

    private fun checkActiveSubscription(): Boolean {
        return if (LocalDataHelper.user?.subscription?.stripe_subscription_id.isNullOrEmpty()) {
            base?.showAlertMessage(
                title = getString(R.string.app_name),
                strMessage = getString(R.string.no_active_plan),
                isCancelable = true,
                positiveText = getString(R.string.purchase),
                negativeText = getString(R.string.skip)
            ) {
                if (it) {
                    navigation?.openPlanListActivity()
                }
            }
            false
        } else {
            true
        }
    }

    override fun onResume() {
        super.onResume()
        setUserData()
    }

    private fun logout() {
        base?.showProgressDialog()
        viewModel.logout().observe(this) {
            it.status.typeCall(success = {
                base?.hideProgressDialog()
                base?.logoutActions()
            }, error = {
                base?.hideProgressDialog()
                base?.logoutActions()
            }, loading = {})
        }
    }


}