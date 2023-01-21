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
import com.app.cityshow.databinding.ActivityEditProfileBinding
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.*
import com.app.cityshow.viewmodel.UserViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileActivity : ActionBarActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: UserViewModel

    private var mProfileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolbar("Update Profile", true)
        setSubTitleText("Ahmedabad")
        binding.clickListener = this
        setUserData()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[UserViewModel::class.java]
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view) {
            binding.imgUploadPhoto -> {
                ImagePicker.with(this)
                    .compress(1024)
                    .cropSquare()     //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            binding.btnSubmit -> {
                hideKeyBoard()
                if (!isValid()) return
                updateProfile()
            }
        }
    }

    /**
     * Set Default User data
     */

    private fun setUserData() {
        val user = LocalDataHelper.user
        binding.edtEmail.setText(user?.email)
        binding.edtFullName.setText(user?.firstName)
        binding.edtLastName.setText(user?.lastname)
        binding.edtNumber.setText(user?.phoneNumber)

        binding.imgProfile.loadImage(user?.full_profile_image, R.drawable.ic_user)
    }

    /**
     * check validation
     */

    private fun isValid(): Boolean {
        var isValid = true

        if (binding.edtFullName.text.isNullOrEmpty()) {
            Validator.setError(binding.tvInputFullName, "Please enter your first name")
            binding.tvInputFullName.requestFocus()
            isValid = false
        }
        if (binding.edtLastName.text.isNullOrEmpty()) {
            Validator.setError(binding.tvInputLastName, "Please enter your last name")
            binding.tvInputLastName.requestFocus()
            isValid = false
        }
        if (binding.edtNumber.text.isNullOrEmpty()) {
            Validator.setError(binding.tvInputNumber, "Please enter your phone number")
            binding.tvInputNumber.requestFocus()
            isValid = false
        } else if (Validator.isPhoneNumberValidate(binding.edtNumber.getTrimText()).not()) {
            Validator.setError(binding.tvInputNumber, "Please enter your valid phone number")
            binding.tvInputNumber.requestFocus()
            isValid = false
        }
        return isValid
    }

    /**
     * Update profile api call
     */

    private fun updateProfile() {
        showProgressDialog()
        val param = HashMap<String, RequestBody>()
        param["first_name"] = binding.edtFullName.getTrimText().toRequestBody()
        param["last_name"] = binding.edtLastName.getTrimText().toRequestBody()
//        param["phone_number"] = binding.edtNumber.getTrimText().toRequestBody()

        var multipartBody: MultipartBody.Part? = null
        if (mProfileUri != null) {
            val file = File(mProfileUri?.path.toString())
            multipartBody = MultipartBody.Part.createFormData(
                "profile_picture",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        }

        viewModel.updateProfile(param, multipartBody).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        LocalDataHelper.user = it.data.data
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    hideProgressDialog()
                    showAlertMessage(it.message)
                }, loading = {})
        }

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    mProfileUri = fileUri
                    binding.imgProfile.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}