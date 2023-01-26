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
import com.app.cityshow.databinding.ActivityAddDiscountBinding
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.Validator
import com.app.cityshow.utility.getTrimText
import com.app.cityshow.utility.requestBody
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.collections.set

class AddDiscountActivity : ActionBarActivity(), View.OnClickListener {
    private var shop: Shop? = null
    private var mProfileUri: Uri? = null
    private lateinit var mBinding: ActivityAddDiscountBinding
    private lateinit var viewModel: ProductViewModel


    override fun initUi() {
        setUpToolbar("Add Discount", true)
        mBinding.clickListener = this
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("SHOP")) {
            shop = intent.getSerializableExtra("SHOP") as Shop
            setShopData(shop)
        }
    }

    private fun setShopData(shop: Shop?) {
        mBinding.shopData = shop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddDiscountBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        when (p0) {
            mBinding.btnSubmit -> {
                hideKeyBoard()
                if (!isValid()) return
                addEditShop()
            }
            mBinding.layDiscountType -> {
                mBinding.spDiscountType.performClick()
            }

            mBinding.ivBanner -> {
                ImagePicker.with(this)
                    .compress(1024)
                    .crop(16f, 9f)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
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

        if (Validator.isEmptyFieldValidate(mBinding.edtDiscountName.getTrimText())) {
            Validator.setError(mBinding.inShopName, getString(R.string.enter_shop_name))
            mBinding.inShopName.requestFocus()
            isValid = false
        }
        if (mBinding.edtCouponCode.text.isNullOrEmpty()) {
            Validator.setError(mBinding.edtDiscountName, getString(R.string.enter_shop_address))
            mBinding.edtDiscountName.requestFocus()
            isValid = false
        }
        if (mBinding.edtNotes.text.isNullOrEmpty()) {
            mBinding.layNotes.requestFocus()
            Validator.setError(mBinding.layNotes, getString(R.string.enter_description))
            isValid = false
        }

        return isValid
    }

    /**
     * Login api call
     * */
    private fun addEditShop() {
        showProgressDialog()
        val param = HashMap<String, RequestBody>()
        param["shop_name"] = mBinding.edtDiscountName.getTrimText().requestBody()
        param["address"] = mBinding.edtCouponCode.getTrimText().requestBody()
        param["notes"] = mBinding.edtNotes.getTrimText().requestBody()
        var multipartBody: MultipartBody.Part? = null
        if (mProfileUri != null) {
            val file = File(mProfileUri!!.path.toString())
            multipartBody = MultipartBody.Part.createFormData(
                "banner_image",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        }
        val images = ArrayList<MultipartBody.Part?>()
        viewModel.addEditShop(param, multipartBody, images).observe(this) {
            it.status.typeCall(
                success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        openHomeActivity()
                    } else {
                        showAlertMessage(strMessage = it.data?.message ?: "")
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mProfileUri = fileUri
                mBinding.ivBanner.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

}