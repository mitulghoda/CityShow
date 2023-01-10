package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.RegisterBinding
import com.app.cityshow.network.typeCall
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.viewmodel.UserViewModel

class RegisterActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: RegisterBinding
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
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSubmit -> {
                hideKeyBoard()
                if (!isValid()) return
                register()
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
        if (binding.edtFullName.text.isNullOrEmpty()) {
            Validator.setError(binding.tvInputFullName, "Please enter your full name")
            binding.tvInputFullName.requestFocus()
            isValid = false
        }
        if (binding.edtPass.text.isNullOrEmpty()) {
            binding.tvInputPass.requestFocus()
            Validator.setError(binding.tvInputPass, "Please enter your password")
            isValid = false
        }

        return isValid
    }

    /**
     * Login api call
     * */
    private fun register() {
        showProgressDialog()
//        getFcmToken { fcmToken, isSuccess ->
//            if (isSuccess) {
        val param = HashMap<String, Any>()
        param["username"] = binding.edtFullName.getTrimText()
        param["email"] = binding.edtEmail.getTrimText()
        param["role"] = "shop_keeper"
        param["password"] = binding.edtPass.getTrimText()
        param["password_confirmation"] = binding.edtPass.getTrimText()

        viewModel.register(param).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        LocalDataHelper.authToken = it.data.data.token
                        LocalDataHelper.user = it.data.data.user
                        LocalDataHelper.login = true
                        openHomeActivity()
                    } else {
                        showAlertMessage(it.message)
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