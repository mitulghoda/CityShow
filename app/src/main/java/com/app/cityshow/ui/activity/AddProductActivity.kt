package com.app.cityshow.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.cityshow.Controller
import com.app.cityshow.R
import com.app.cityshow.databinding.AddProductBinding
import com.app.cityshow.model.FootwearSizeModel
import com.app.cityshow.model.category.Category
import com.app.cityshow.model.category.SubCategory
import com.app.cityshow.model.product.Product
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.adapter.EditTextAdapter
import com.app.cityshow.ui.adapter.FootwearSizeAdapter
import com.app.cityshow.ui.adapter.ImageAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetCategories
import com.app.cityshow.ui.bottomsheet.BottomSheetShops
import com.app.cityshow.ui.bottomsheet.BottomSheetSubCategories
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.utility.*
import com.app.cityshow.viewmodel.ProductViewModel
import com.bumptech.glide.Glide
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
import com.google.gson.Gson
import com.stfalcon.imageviewer.StfalconImageViewer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddProductActivity : ActionBarActivity(), View.OnClickListener {
    private var strGender: String = ""
    private var strGuranty: String = ""
    private var strGold: String = ""
    private var strShopId: String? = null
    private var strCategoryId: String? = null
    private var strSubCategory: String? = null
    private lateinit var mBinding: AddProductBinding
    private var mAssetImages = ArrayList<Media>()
    private var deletedImagesId = ArrayList<String>()
    private var mKeyFeature = ArrayList<String>()
    var assetImageAdapter = ImageAdapter()
    var editTextAdapter = EditTextAdapter()
    private var mArrayList: ArrayList<Category>? = null
    private var mSubCategoryArrayList = ArrayList<SubCategory>()
    private var shopList: ArrayList<Shop>? = null
    private lateinit var viewModel: ProductViewModel

    private var footwearSizeAdapter: FootwearSizeAdapter? = null
    val filterFootwearSizeList: ArrayList<String> = ArrayList()

    var productData: Product? = null

    override fun initUi() {
        productData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("data", Product::class.java)
        } else intent.getSerializableExtra("data") as Product?

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        val data = RegionManager.getCategoryWiseView()
        Log.e("CATEGORY_VIEW", gson.toJson(data))
        assetImageAdapter.arrayList = mAssetImages
        editTextAdapter.arrayList = mKeyFeature
        assetImageAdapter.clickCallback = this::onAssetImageClick
        assetImageAdapter.deleteCallback = this::onAssetImageDelete
        editTextAdapter.deleteCallback = this::onKeyFeature
        mBinding.rvPhoto.adapter = assetImageAdapter
        mBinding.rvKeyFeature.adapter = editTextAdapter
        callGetCategoryApi()
        callGetMyShop()
        mBinding.rgGender.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.id) {
                mBinding.rbMale.id -> {
                    strGender = mBinding.rbMale.text.toString()
                }
                mBinding.rbFemale.id -> {
                    strGender = mBinding.rbFemale.text.toString()
                }
                mBinding.rbChild.id -> {
                    strGender = mBinding.rbChild.text.toString()
                }
            }
        }
        mBinding.rbGuaranty.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.id) {
                mBinding.rbGuarantyYes.id -> {
                    strGuranty = "Yes"
                }
                mBinding.rbGuarantyNo.id -> {
                    strGuranty = "No"
                }
            }
        }
        mBinding.rgGold.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.id) {
                mBinding.rbMale.id -> {
                    strGold = mBinding.rbYes.text.toString()
                }
                mBinding.rbFemale.id -> {
                    strGold = mBinding.rbNo.text.toString()
                }
            }
        }
        setProductData()
    }

    private fun setProductData() {
        mBinding.data = productData
        strCategoryId = productData?.cat_id
        strSubCategory = productData?.subcat_id
        strShopId = productData?.shop?.id
        productData?.product_image?.forEach {
            mAssetImages.add(Media(it.image_url, it.id))
        }
        assetImageAdapter.notifyDataSetChanged()
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
                    val shop = shopList!!.filter { it1 -> it1.id == productData?.shopkeeper_id }
                    Log.e("Shop Size", shop.size.toString())
                    if (shop.size > 1) {
                        mBinding.edtShops.setText(shop[0].shop_name)
                    }
                } else {
                    showAlertMessage(it.message)
                }
            }, error = {
                hideProgressDialog()
                showAlertMessage(it.message)
            }, loading = {})
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AddProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Add product".takeIf { productData == null } ?: "Update product", true)
        setFootwearAdapter()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            mBinding.tvAdditional -> {
                if (mBinding.layAdditional.isVisible()) {
                    mBinding.layAdditional.gone()
                } else {
                    mBinding.layAdditional.show()
                }
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
            mBinding.edtCategory -> {
                BottomSheetCategories.newInstance(getString(R.string.select_category),
                    mArrayList!!,
                    object : BottomSheetCategories.BottomSheetItemClickListener {
                        override fun onItemClick(data: Category) {
                            mBinding.edtCategory.setText(data.name)
                            strCategoryId = data.id
                            mBinding.edtSubCategory.setText("")
                            strSubCategory = ""
                            mSubCategoryArrayList.clear()
                            mSubCategoryArrayList.addAll(data.sub_category)
                            setCategoryAdditionalData(
                                data.name.replace("  ", " ").lowercase().trim()
                            )
                        }
                    }).show(this)
            }
            mBinding.edtSubCategory -> {
                BottomSheetSubCategories.newInstance(getString(R.string.select_sub_category),
                    mSubCategoryArrayList,
                    object : BottomSheetSubCategories.BottomSheetItemClickListener {
                        override fun onItemClick(data: SubCategory) {
                            mBinding.edtSubCategory.setText(data.name)
                            strSubCategory = data.id
                        }
                    }).show(this)
            }
            mBinding.btnSubmit -> {
                if (checkValidation()) {
                    addEditProduct()
                }
            }
            mBinding.cardAddImage -> {
                openImageFilePicker(object : FilePickerCallback {
                    override fun onSuccess(media: Media?) {
                        if (media == null) return
                        mAssetImages.add(media)
                        assetImageAdapter.notifyDataSetChanged()
                        editTextAdapter.notifyDataSetChanged()
                    }

                    override fun onSuccess(mediaList: ArrayList<Media>?) {
                        if (mediaList.isNullOrEmpty()) return
                        mAssetImages.addAll(mediaList)
                        assetImageAdapter.notifyDataSetChanged()
                        editTextAdapter.notifyDataSetChanged()
                    }

                    override fun onError(error: String?) {
                        toast(error)
                    }
                })
            }
            mBinding.txtKeyFeature -> {
                editTextAdapter.setItem(0, "")
                editTextAdapter.notifyDataSetChanged()
            }
            mBinding.layoutFootwear -> {
                if (mBinding.expandableLayoutPlaces.isExpanded) {
                    mBinding.expandableLayoutPlaces.isExpanded = false
                    mBinding.imgDownArrow.rotation = 0f
                } else {
                    mBinding.expandableLayoutPlaces.isExpanded = true
                    mBinding.imgDownArrow.rotation = 180f
                }
            }
        }
    }

    fun setCategoryAdditionalData(categoryName: String) {
        Log.e(categoryName)
        Log.e(getString(R.string.mobile_accessories))
        Log.e("${RegionManager.getCategoryWiseView()}")
        val data = RegionManager.getCategoryWiseView()?.filter {
            categoryName.lowercase().equals(it.category.lowercase(), true)
        }
        if ((data?.size ?: 0) <= 0) return
        hideAllAdditionalInfo()
        when (data?.get(0)?.category?.lowercase()) {
            getString(R.string.footwear).lowercase() -> {
                mBinding.layGender.show()
                val sizeList = ArrayList<FootwearSizeModel>()
                val min = data[0].required_details[2].min
                val max = data[0].required_details[2].max
                for (i in min..max) {
                    sizeList.add(FootwearSizeModel("$i"))
                }
                footwearSizeAdapter?.setData(sizeList)
            }
            getString(R.string.jwellry).lowercase() -> {
                mBinding.layGold.show()
            }
            getString(R.string.mobile_accessories).lowercase() -> {
                mBinding.layMobileAccessories.show()
            }
            getString(R.string.cloths).lowercase() -> {
                mBinding.layCloth.show()
            }
            getString(R.string.electronics).lowercase() -> {
                mBinding.layElectronics.show()
            }
        }
    }

    private fun hideAllAdditionalInfo() {
        mBinding.layGender.hide()
        mBinding.layGold.hide()
        mBinding.layMobileAccessories.hide()
        mBinding.layCloth.hide()
        mBinding.layElectronics.hide()
    }

    private fun setFootwearAdapter(minValue: Int = 5, maxValue: Int = 11) {
        val sizeList = ArrayList<FootwearSizeModel>()
        for (i in minValue..maxValue) {
            sizeList.add(FootwearSizeModel("$i"))
        }

        footwearSizeAdapter = FootwearSizeAdapter(sizeList) { id, position, data ->
            filterFootwearSizeList.clear()
            for (i in sizeList.filter { it.isCheck }) {
                filterFootwearSizeList.add("\"${i.name.toString()}\"")
            }
        }
        mBinding.rvFootwearSize.adapter = footwearSizeAdapter
    }

    private fun checkValidation(): Boolean {
        if (Validator.isEmptyFieldValidate(mBinding.edtName.getTrimText())) {
            Validator.setError(mBinding.edtName, getString(R.string.enter_product_name))
            mBinding.edtName.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtBrandName.getTrimText())) {
            Validator.setError(mBinding.etBrandName, getString(R.string.enter_brand_name))
            mBinding.etBrandName.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtModel.getTrimText())) {
            Validator.setError(mBinding.edtModel, getString(R.string.enter_model_name))
            mBinding.edtModel.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtOriginalPrice.getTrimText())) {
            Validator.setError(mBinding.edtOriginalPrice, getString(R.string.enter_original_price))
            mBinding.edtOriginalPrice.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtSellingPrice.getTrimText())) {
            Validator.setError(mBinding.edtSellingPrice, getString(R.string.enter_selling_price))
            mBinding.edtSellingPrice.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtOriginalPrice.getTrimText())) {
            Validator.setError(mBinding.edtOriginalPrice, getString(R.string.enter_original_price))
            mBinding.edtOriginalPrice.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtOriginalPrice.getTrimText())) {
            Validator.setError(mBinding.edtOriginalPrice, getString(R.string.enter_original_price))
            mBinding.edtOriginalPrice.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtOriginalPrice.getTrimText())) {
            Validator.setError(mBinding.edtOriginalPrice, getString(R.string.enter_original_price))
            mBinding.edtOriginalPrice.requestFocus()
            return false
        } else if (Validator.isEmptyFieldValidate(mBinding.edtDesc.getTrimText())) {
            Validator.setError(mBinding.edtDesc, getString(R.string.enter_description))
            mBinding.edtDesc.requestFocus()
            return false
        } else if (mAssetImages.isEmpty()) {
            showToast(getString(R.string.select_product_photos))
            return false
        } else if (strShopId.isNullOrEmpty()) {
            showToast(getString(R.string.select_shop))
            return false
        }
        return true
    }

    private fun callGetCategoryApi() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        viewModel.getCategories(param).observe(this) {
            it.status.typeCall(success = {
                hideProgressDialog()
                if (it.data != null && it.data.success) {
                    Log.e("CATEGORIES", Gson().toJson(it.data.data))
                    mArrayList = it.data.data.categories as ArrayList<Category>?
                    val category =
                        mArrayList?.filter { category -> category.equals(productData?.cat_id) }
                            ?.get(0)
                    val subCategory =
                        category?.sub_category?.filter { subCategory ->
                            subCategory.equals(
                                productData?.subcat_id
                            )
                        }?.get(0)
                    strCategoryId = subCategory?.category_id
                    strSubCategory = subCategory?.id
                    mBinding.edtCategory.setText(category?.name)
                    mBinding.edtSubCategory.setText(subCategory?.name)
                } else {
                    showAlertMessage("", it.message)
                }
            }, error = {
                hideProgressDialog()
                showAlertMessage("", it.message)
            }, loading = {})
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun addEditProduct() {
        showProgressDialog()
        val param = HashMap<String, RequestBody>()
        param["shop_keeper_id"] = LocalDataHelper.user?.id!!.requestBody()
        param["category_id"] = strCategoryId!!.requestBody()
        param["sub_category_id"] = strSubCategory!!.requestBody()
        param["product_name"] = mBinding.edtName.getTrimText().requestBody()
        param["brand_name"] = mBinding.edtBrandName.getTrimText().requestBody()
        param["model_name"] = mBinding.edtModel.getTrimText().requestBody()
        param["product_price"] = mBinding.edtOriginalPrice.getTrimText().requestBody()
        param["product_selling_price"] = mBinding.edtSellingPrice.getTrimText().requestBody()
        param["gender"] = strGender.requestBody()
//        param["size[]"] = mBinding.edtName.getTrimText().requestBody()
        param["color"] = mBinding.edtColor.getTrimText().requestBody()
        param["material"] = mBinding.edtMaterial.getTrimText().requestBody()
        param["weight"] = mBinding.edtGoldWeight.getTrimText().requestBody()
        param["is_gold"] = strGold.requestBody()
        param["device_os"] = mBinding.edtOs.getTrimText().requestBody()
        param["ram"] = mBinding.edtRam.getTrimText().requestBody()
        param["storage"] = mBinding.edtStorage.getTrimText().requestBody()
        param["connectivity"] = mBinding.edtConnect.getTrimText().requestBody()
        param["key_feature[]"] = Gson().toJson(editTextAdapter.arrayList).requestBody()
        param["description"] = mBinding.edtDesc.getTrimText().requestBody()
        param["warranty"] = mBinding.cbWarranty.isChecked.toString().requestBody()
        param["guaranty"] = strGuranty.requestBody()
        if (deletedImagesId.isNotEmpty()) {
            Log.e("DELETED_IMAGES_ID", TextUtils.join(",", deletedImagesId))
            param["deletedImagesId"] = TextUtils.join(",", deletedImagesId).toString().requestBody()
        }
        param["shop_id[]"] = strShopId!!.requestBody()
        if (mBinding.cbXXL.isChecked) param["size[0]"] =
            mBinding.cbXXL.text.toString().requestBody()
        if (mBinding.cbxl.isChecked) param["size[1]"] = mBinding.cbxl.text.toString().requestBody()
        if (mBinding.cbS.isChecked) param["size[2]"] = mBinding.cbS.text.toString().requestBody()
        if (mBinding.cbL.isChecked) param["size[3]"] = mBinding.cbL.text.toString().requestBody()

        Log.e(filterFootwearSizeList.joinToString())
        param["footwear_size"] = filterFootwearSizeList.joinToString().toRequestBody()

        val images = ArrayList<MultipartBody.Part?>()
        if (mAssetImages.isNotEmpty()) {
            mAssetImages.forEachIndexed { i, media ->
                if (!media.url.startsWith("http", true)) {
                    val file = File(media.mediaFile.toString())
                    var multipartBody: MultipartBody.Part?
                    multipartBody = MultipartBody.Part.createFormData(
                        "images[$i]", file.name, file.asRequestBody("image/*".toMediaType())
                    )
                    images.add(multipartBody)
                }

            }
        }

        if (productData != null) {
            param["id"] = productData!!.id.orEmpty().toRequestBody()
            viewModel.updateProduct(param, images).observe(this) {
                it.status.typeCall(success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        finish()
                    } else {
                        showAlertMessage(it.message)
                    }
                }, error = {
                    hideProgressDialog()
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = {})
            }
        } else {
            viewModel.createProduct(param, images).observe(this) {
                it.status.typeCall(success = {
                    hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        finish()
                    } else {
                        showAlertMessage(it.message)
                    }
                }, error = {
                    hideProgressDialog()
                    showAlertMessage(getString(R.string.something_went_wrong))
                }, loading = {})
            }
        }

//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun onAssetImageClick(media: Media, position: Int) {
        StfalconImageViewer.Builder(this, mAssetImages) { view, image ->
            Glide.with(mBinding.rvPhoto).load(image.url).into(view)
        }.withStartPosition(position).show()
    }

    private fun onAssetImageDelete(media: Media, position: Int) {
        if (media.url.startsWith("http", true)) {
            deletedImagesId.add(media.id)
        }
        mAssetImages.removeAt(position)
        assetImageAdapter.notifyItemRemoved(position)
        editTextAdapter.notifyItemRemoved(position)
    }

    private fun onKeyFeature(media: String, position: Int) {
        mKeyFeature.removeAt(position)
        editTextAdapter.notifyItemRemoved(position)
    }
}