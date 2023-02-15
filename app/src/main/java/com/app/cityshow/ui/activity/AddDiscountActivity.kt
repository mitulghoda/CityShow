package com.app.cityshow.ui.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.ActivityAddDiscountBinding
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.model.product.Product
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.bottomsheet.BottomSheetProducts
import com.app.cityshow.ui.bottomsheet.BottomSheetShops
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.*
import com.app.cityshow.viewmodel.ProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.collections.set

class AddDiscountActivity : ActionBarActivity(), View.OnClickListener {
    private var strDiscountType: String = "Yes"
    private var strProductId: String? = null
    private var strShopId: String? = null
    private var strstartDate: String? = null
    private var strEndDate: String? = null
    private var shop: Shop? = null
    private var discount: Discount? = null
    private var discountBannerImage: Uri? = null
    private lateinit var mBinding: ActivityAddDiscountBinding
    private lateinit var viewModel: ProductViewModel

    private var shopList: ArrayList<Shop>? = null
    private var productsList: ArrayList<Product>? = null
    override fun initUi() {
        setUpToolbar("Add Discount", true)
        discount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("DISCOUNT", Discount::class.java)
        } else {
            intent.getSerializableExtra("DISCOUNT") as Discount?
        }
        mBinding.clickListener = this

        if (discount != null) {
            mBinding.discount = discount
            mBinding.rbYes.isChecked = discount?.is_price.equals("yes", true)
            mBinding.rbNo.isChecked = !discount?.is_price.equals("yes", true)
        }
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetMyShop()
        callGetMyProductList()
        mBinding.rbgType.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                mBinding.rbYes.id -> {
                    strDiscountType = mBinding.rbYes.text.toString()
                }
                mBinding.rbNo.id -> {
                    strDiscountType = mBinding.rbNo.text.toString()
                }
            }
        }
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
                addEditDiscount()
            }
            mBinding.layDiscountType -> {
                mBinding.spDiscountType.performClick()
            }
            mBinding.tvStartDate -> {
                openDatePickerDialog(",", { view, year, month, dayOfMonth ->
                    val stringBundle = java.lang.StringBuilder()
                    stringBundle.append(dayOfMonth)
                    stringBundle.append("-")
                    stringBundle.append(month + 1)
                    stringBundle.append("-")
                    stringBundle.append(year)
                    strstartDate = DateTimeUtil.formatDate(
                        "dd-mm-yyyy",
                        "yyyy-mm-dd",
                        stringBundle.toString()
                    )
                    Log.e("END_DATE", "$strstartDate")
                    mBinding.tvStartDate.text = DateTimeUtil.formatDate(
                        "dd-MM-yyyy",
                        "dd MMMM, yyyy",
                        stringBundle.toString()
                    )
                }, isFuture = true, isPast = false)
            }
            mBinding.tvEndDate -> {
                openDatePickerDialog(",", { view, year, month, dayOfMonth ->
                    val stringBundle = java.lang.StringBuilder()
                    stringBundle.append(dayOfMonth)
                    stringBundle.append("-")
                    stringBundle.append(month + 1)
                    stringBundle.append("-")
                    stringBundle.append(year)
                    strEndDate = DateTimeUtil.formatDate(
                        "dd-mm-yyyy",
                        "yyyy-mm-dd",
                        stringBundle.toString()
                    )
                    Log.e("END_DATE", "$strEndDate")
                    mBinding.tvEndDate.text = DateTimeUtil.formatDate(
                        "dd-MM-yyyy",
                        "dd MMMM, yyyy",
                        stringBundle.toString()
                    )
                }, isFuture = true, isPast = false)
            }
            mBinding.layDiscountType -> {
                mBinding.spDiscountType.performClick()
            }
            mBinding.edtShops -> {
                BottomSheetShops.newInstance(getString(R.string.select_shop),
                    shopList!!,
                    object : BottomSheetShops.BottomSheetItemClickListener {
                        override fun onItemClick(data: Shop) {
                            mBinding.edtShops.setText(data.shop_name)
                            strShopId = data.id
                        }
                    }).show(this)
            }
            mBinding.edtProduct -> {
                BottomSheetProducts.newInstance(getString(R.string.select_product),
                    productsList!!,
                    object : BottomSheetProducts.BottomSheetItemClickListener {
                        override fun onItemClick(data: Product) {
                            mBinding.edtProduct.setText(data.name)
                            strProductId = data.id
                        }
                    }).show(this)
            }

            mBinding.ivBanner -> {
                ImagePicker.with(this).compress(1024)
                    .crop(16f, 9f)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080, 1080
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
        }

    }

    private fun callGetMyProductList() {
        val param = HashMap<String, Any>()
        param["user_id"] = LocalDataHelper.userId
        viewModel.myProduct(param).observe(this) {
            it.status.typeCall(success = {
                if (it.data != null && it.data.success) {
                    val list = it.data.data.products
                    productsList = list
                    val product = Product()
                    product.name = "Apply on all product"
                    productsList?.add(0, product)
                }
            }, error = {}, loading = { })
        }

    }

    private fun callGetMyShop() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        viewModel.myShops(param).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                if (it.data != null && it.data.success) {
                    Log.e("CATEGORIES", Gson().toJson(it.data.data))
                    shopList = it.data.data.shops as ArrayList<Shop>
                    val shop = Shop()
                    shop.shop_name = "Apply for all shops"
                    shopList?.add(0, shop)
                } else {
                    showAlertMessage(it.message)
                }
            }, error = {
                hideProgressDialog()
                showAlertMessage(it.message)
            }, loading = {})
        }
    }

    /**
     * check validation
     * */
    private fun isValid(): Boolean {
        var isValid = true

        if (Validator.isEmptyFieldValidate(mBinding.edtDiscountName.getTrimText())) {
            Validator.setError(mBinding.inShopName, getString(R.string.enter_disount_name))
            mBinding.inShopName.requestFocus()
            isValid = false
        }
        if (mBinding.edtCouponCode.text.isNullOrEmpty()) {
            Validator.setError(mBinding.edtCouponCode, getString(R.string.enter_disount_code))
            mBinding.edtCouponCode.requestFocus()
            isValid = false
        }
        if (strDiscountType.isNullOrEmpty()) {
            toast("Select discount type")
            isValid = false
        }
        if (strstartDate.isNullOrEmpty() && strEndDate.isNullOrEmpty()) {
            toast("Select start and end date of discount")
            isValid = false
        }
        if (mBinding.edtDiscount.text.isNullOrEmpty()) {
            mBinding.edtDiscount.requestFocus()
            Validator.setError(mBinding.edtDiscount, getString(R.string.enter_discount))
            isValid = false
        }
        if (mBinding.edtNotes.text.isNullOrEmpty()) {
            mBinding.layNotes.requestFocus()
            Validator.setError(mBinding.layNotes, getString(R.string.enter_description))
            isValid = false
        }
        if (discountBannerImage == null) {
            toast("Select discount banner image")
            isValid = false
        }

        return isValid
    }

    /**
     * Login api call
     * */
    private fun addEditDiscount() {
        showProgressDialog()
        val param = HashMap<String, RequestBody>()
        param["coupon_name"] = mBinding.edtDiscountName.getTrimText().requestBody()
        param["coupon_code"] = mBinding.edtCouponCode.getTrimText().requestBody()
        param["is_price"] = strDiscountType.requestBody()
        param["discount"] = mBinding.edtDiscount.getTrimText().requestBody()
        param["shop_id"] = (strShopId ?: "").requestBody()
        param["product_id"] = (strProductId ?: "").requestBody()
        param["notes"] = mBinding.edtNotes.getTrimText().requestBody()
        param["start_date"] = strstartDate!!.requestBody()
        param["end_date"] = strEndDate!!.requestBody()

        if (discount != null) {
            param["id"] = (discount?.id ?: "").requestBody()
        }

        var multipartBody: MultipartBody.Part? = null
        if (discountBannerImage != null) {
            val file = File(discountBannerImage!!.path.toString())
            multipartBody = MultipartBody.Part.createFormData(
                "image", file.name, file.asRequestBody("image/*".toMediaType())
            )
        }
        val images = ArrayList<MultipartBody.Part?>()
        viewModel.addEditDiscount(param, multipartBody, images).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                if (it.data != null && it.data.success) {
                    toast(it.data.message)
                    finish()
                } else {
                    showAlertMessage(strMessage = it.data?.message ?: "")
                }
            }, error = {
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

                discountBannerImage = fileUri
                mBinding.ivBanner.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
        }

}