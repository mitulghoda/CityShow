package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.OtpViewBinding
import com.app.cityshow.network.typeCall
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getString
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.viewmodel.UserViewModel

class OTPActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: OtpViewBinding
    private lateinit var viewModel: UserViewModel
    var email: String? = ""
    override fun initUi() {
        email = intent.getStringExtra("email")

        binding.clickListener = this
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtpViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(p0: View?) {

        when (p0) {
            binding.btnVerify -> {
                hideKeyBoard()
                if (!isValid()) return
                verifyOTP()
            }
            binding.ivBack -> {
                finish()
            }
        }
    }
    /**
     * check validation
     * */
    private fun isValid(): Boolean {
        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtEmail.getTrimText())) {
            Validator.setError(binding.tvInputEmail, "Please enter OTP")
            binding.tvInputEmail.requestFocus()
            isValid = false
        }
        return isValid
    }

    /**
     * Login api call
     * */
    private fun verifyOTP() {
        showProgressDialog()
//        getFcmToken { fcmToken, isSuccess ->
//            if (isSuccess) {
        val param = HashMap<String, Any>()
        param["email"] = email.orEmpty()
        param["otp"] = binding.edtEmail.getTrimText()
        viewModel.verifyOtp(param).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        openChangePasswordActivity(binding.edtEmail.getTrimText())
                        finish()
                    } else {
                        showAlertMessage(it.data?.message)
                    }
                },
                error = {
                    showAlertMessage(getString(R.string.something_went_wrong))
                }
            )
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }
}