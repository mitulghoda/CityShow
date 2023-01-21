package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ChangePasswordBinding
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.UserViewModel

class ChangePasswordActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: ChangePasswordBinding
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
        binding = ChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSubmit -> {
                hideKeyBoard()
                if (!isValid()) return
                changePassword()
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

        if (Validator.isEmptyFieldValidate(binding.edtPassword.getTrimText())) {
            Validator.setError(binding.tvInputPassword, "Please enter new password")
            binding.tvInputPassword.requestFocus()
            isValid = false
        } else if (Validator.isEmptyFieldValidate(binding.edtReEnterPassword.getTrimText())) {
            Validator.setError(binding.tvInputReEnterPassword, "Please enter confirm password")
            binding.tvInputReEnterPassword.requestFocus()
            isValid = false
        } else if (!binding.edtReEnterPassword.getTrimText()
                .equals(binding.edtPassword.getTrimText(), true)
        ) {
            Validator.setError(binding.tvInputReEnterPassword,
                "Please enter correct confirm password")
            binding.tvInputReEnterPassword.requestFocus()
            isValid = false
        }
        return isValid
    }

    /**
     * Login api call
     * */
    private fun changePassword() {
        showProgressDialog()
//        getFcmToken { fcmToken, isSuccess ->
//            if (isSuccess) {
        val param = HashMap<String, Any>()
        param["email"] = email.orEmpty()
        param["password"] = binding.edtPassword.getTrimText()
        param["password_confirmation"] = binding.edtReEnterPassword.getTrimText()

        viewModel.changePassword(param).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        openLoginActivity()
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