package com.app.cityshow.ui.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RegisterActivity : NavigationActivity(), View.OnClickListener {
    private var mProfileUri: Uri? = null
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
            binding.layPhoto -> {
                ImagePicker.with(this)
                    .compress(1024)
                    .cropSquare()         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080,
                        1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
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
        var multipartBody: MultipartBody.Part? = null
        if (mProfileUri != null) {
            val file = File(mProfileUri!!.path.toString())
            multipartBody = MultipartBody.Part.createFormData(
                "profile_pic",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        }
        viewModel.register(param, multipartBody).observe(this) {
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mProfileUri = fileUri
                binding.imgProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}