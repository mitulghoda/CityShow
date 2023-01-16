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
import com.app.cityshow.databinding.ActivityAddShopBinding
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.ui.adapter.ImageAdapter
import com.app.cityshow.ui.common.ActionBarActivity
import com.app.cityshow.ui.common.NavigationActivity
import com.app.cityshow.utility.*
import com.app.cityshow.viewmodel.ProductViewModel
import com.app.cityshow.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
import com.github.dhaval2404.imagepicker.ImagePicker
import com.stfalcon.imageviewer.StfalconImageViewer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddShopActivity : ActionBarActivity(), View.OnClickListener {
    private var shop: Shop? = null
    private var mProfileUri: Uri? = null
    private lateinit var mBinding: ActivityAddShopBinding
    private lateinit var viewModel: ProductViewModel

    private var mAssetImages = ArrayList<Media>()
    var assetImageAdapter = ImageAdapter()


    override fun initUi() {
        setUpToolbar("Add Shop", true)
        mBinding.clickListener = this
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        if (intent.hasExtra("SHOP")) {
            shop = intent.getSerializableExtra("SHOP") as Shop
            setShopData(shop)
        }
        assetImageAdapter.arrayList = mAssetImages
        assetImageAdapter.clickCallback = this::onAssetImageClick
        assetImageAdapter.deleteCallback = this::onAssetImageDelete
        mBinding.rvPhoto.adapter = assetImageAdapter
    }

    private fun setShopData(shop: Shop?) {
        mBinding.shopData = shop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddShopBinding.inflate(layoutInflater)
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
            mBinding.ivBanner -> {
                ImagePicker.with(this)
                    .compress(1024)
                    .crop(200f, 100f)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080,
                        1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            mBinding.cardAddImage -> {
                openImageFilePicker(object : FilePickerCallback {
                    override fun onSuccess(media: Media?) {
                        if (media == null) return
                        mAssetImages.add(media)
                    }

                    override fun onSuccess(mediaList: ArrayList<Media>?) {
                        if (mediaList.isNullOrEmpty()) return
                        mAssetImages = mediaList
                        assetImageAdapter.setData(mediaList)
                    }

                    override fun onError(error: String?) {
                        toast(error)
                    }
                })
            }

        }

    }

    /**
     * check validation
     * */
    private fun isValid(): Boolean {
        var isValid = true

        if (Validator.isEmptyFieldValidate(mBinding.edtShopName.getTrimText())) {
            Validator.setError(mBinding.inShopName, getString(R.string.enter_shop_name))
            mBinding.inShopName.requestFocus()
            isValid = false
        }
        if (mBinding.edtAddress.text.isNullOrEmpty()) {
            Validator.setError(mBinding.layAddress, getString(R.string.enter_shop_address))
            mBinding.layAddress.requestFocus()
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
        param["shop_name"] = mBinding.edtShopName.getTrimText().requestBody()
        param["address"] = mBinding.edtAddress.getTrimText().requestBody()
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
        mAssetImages.forEach { media ->
            val file = File(media.mediaFile.toString())
            val tempMultipartBody = MultipartBody.Part.createFormData(
                "images[]",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
            images.add(tempMultipartBody)
        }
        viewModel.addEditShop(param, multipartBody, images).observe(this) {
            hideProgressDialog()
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        openHomeActivity()
                    } else {
                        showAlertMessage(it.message)
                    }
                },
                error = {
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

    private fun onAssetImageClick(media: Media, position: Int) {
        StfalconImageViewer.Builder(this, mAssetImages) { view, image ->
            Glide.with(mBinding.rvPhoto).load(image.url).into(view)
        }.withStartPosition(position).show()
    }

    private fun onAssetImageDelete(media: Media, position: Int) {
        mAssetImages.removeAt(position)
        assetImageAdapter.notifyItemRemoved(position)
    }
}