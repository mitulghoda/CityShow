package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.BuildConfig
import com.app.cityshow.Controller
import com.app.cityshow.databinding.LoginBinding
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.UserViewModel

class LoginActivity : NavigationActivity(), View.OnClickListener {
    private lateinit var binding: LoginBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        binding.clickListener = this
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]

        if (BuildConfig.DEBUG) {
            binding.edtEmail.setText("mitul02@mailinator.com")
            binding.edtPassword.setText("admin@123")
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                hideKeyBoard()
                if (!isValid()) return
                login()
            }
            binding.tvForgot -> {
                openForgotPasswordActivity()
            }
            binding.btnSign -> {
                openRegisterActivity()
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
            isValid = false
        } else if (!Validator.isValidEmail(binding.edtEmail.getTrimText())) {
            Validator.setError(binding.tvInputEmail, "Please enter valid email")
            isValid = false
        }
        if (binding.edtPassword.text.isNullOrEmpty()) {
            Validator.setError(binding.tvInputPassword, "Please enter your password")
            isValid = false
        }

        return isValid
    }

    /**
     * Login api call
     * */
    private fun login() {
        showProgressDialog()
//        getFcmToken { fcmToken, isSuccess ->
//            if (isSuccess) {
        val param = HashMap<String, Any>()
        param["email"] = binding.edtEmail.getTrimText()
        param["password"] = binding.edtPassword.getTrimText()
//                param["device_type"] = NetworkURL.DEVICE_TYPE_ANDROID
//                param["device_id"] = fcmToken

        viewModel.login(param).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        if (it.data.data.user != null) {
                            LocalDataHelper.authToken = it.data.data.token
                            LocalDataHelper.user = it.data.data.user
                            LocalDataHelper.login = true
                            openHomeActivity()
                        } else {
                            showAlertMessage("", it.data.message)
                        }
                    } else {
                        showAlertMessage("", it.data?.message ?: "")
                    }
                },
                error = {
                    showAlertMessage("", it.message)
                }, loading = {})
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

}