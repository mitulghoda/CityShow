package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ForgotPasswordBinding
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.UserViewModel

class ForgotPasswordActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: ForgotPasswordBinding
    private lateinit var viewModel: UserViewModel
    override fun initUi() {
        binding.clickListener = this
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSubmit -> {
                hideKeyBoard()
                if (!isValid()) return
                forgotPassword()
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
            Validator.setError(binding.tvInputEmail, "Please enter email")
            binding.tvInputEmail.requestFocus()
            isValid = false
        } else if (!Validator.isValidEmail(binding.edtEmail.getTrimText())) {
            Validator.setError(binding.tvInputEmail, "Please enter valid email")
            binding.tvInputEmail.requestFocus()
            isValid = false
        }
        return isValid
    }

    /**
     * Login api call
     * */
    private fun forgotPassword() {
        showProgressDialog()
//        getFcmToken { fcmToken, isSuccess ->
//            if (isSuccess) {
        val param = HashMap<String, Any>()
        param["email"] = binding.edtEmail.getTrimText()
        viewModel.sendForgot(param).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        openOTPActivity(binding.edtEmail.getTrimText())
                        finish()
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = {})
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }
}