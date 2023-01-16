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
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
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

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance))[UserViewModel::class.java]
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view) {
            binding.imgUploadPhoto -> {
                ImagePicker.with(this)
                    .compress(1024)
                    .cropSquare()     //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080,
                        1080)  //Final image resolution will be less than 1080 x 1080(Optional)
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
        binding.edtFullName.setText(user?.username)

        Glide.with(this)
            .load(user?.profilePic)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgProfile)
    }

    /**
     * check validation
     */

    private fun isValid(): Boolean {
        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtFullName.getTrimText())) {
            Validator.setError(binding.tvInputFullName, "Please enter name")
            isValid = false
        }
        if (Validator.isEmptyFieldValidate(binding.edtEmail.getTrimText())) {
            Validator.setError(binding.tvInputEmail, "Please enter email")
            isValid = false
        } else if (!Validator.isValidEmail(binding.edtEmail.getTrimText())) {
            Validator.setError(binding.tvInputEmail, "Please enter valid email")
            isValid = false
        }
        /*if (Validator.isEmptyFieldValidate(binding.edtCountryCode.selectedCountryName)) {
            toast("Please select any country code")
            isValid = false
        }
        if (Validator.isEmptyFieldValidate(binding.edtNumber.getTrimText())) {
            Validator.setError(binding.tvInputNumber, "Please enter number")
            isValid = false
        } else if (!Validator.isPhoneNumberValidate(binding.edtNumber.getTrimText())) {
            Validator.setError(binding.tvInputNumber, "Please enter valid number")
            isValid = false
        }
        if (Validator.isEmptyFieldValidate(binding.edtAddress.getTrimText())) {
            Validator.setError(binding.tvInputAddress, "Please enter address")
            isValid = false
        }*/

        return isValid
    }

    /**
     * Update profile api call
     */

    private fun updateProfile() {
        showProgressDialog()
        val param = HashMap<String, RequestBody>()
        param["username"] = binding.edtFullName.getTrimText().toRequestBody()
        param["email"] = binding.edtEmail.getTrimText().toRequestBody()

        var multipartBody: MultipartBody.Part? = null
        if (mProfileUri != null) {
            val file = File(mProfileUri!!.path.toString())
            multipartBody = MultipartBody.Part.createFormData(
                "profile_pic",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        }

        viewModel.updateProfile(param, multipartBody).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        LocalDataHelper.user = it.data.data
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
                    showAlertMessage(it.message)
                }, loading = {})
        }

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