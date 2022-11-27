package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import com.app.cityshow.R
import com.app.cityshow.databinding.AddProductBinding
import com.app.cityshow.ui.adapter.EditTextAdapter
import com.app.cityshow.ui.adapter.ImageAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetCommonPopup
import com.bumptech.glide.Glide
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
import com.app.cityshow.ui.common.ActionBarActivity
import com.stfalcon.imageviewer.StfalconImageViewer
import java.util.ArrayList

class AddProductActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: AddProductBinding
    private var mAssetImages = ArrayList<Media>()
    private var mKeyFeature = ArrayList<String>()
    var assetImageAdapter = ImageAdapter()
    var editTextAdapter = EditTextAdapter()
    override fun initUi() {
        assetImageAdapter.arrayList = mAssetImages
        editTextAdapter.arrayList = mKeyFeature
        assetImageAdapter.clickCallback = this::onAssetImageClick
        assetImageAdapter.deleteCallback = this::onAssetImageDelete
        editTextAdapter.deleteCallback = this::onKeyFeature
        mBinding.rvPhoto.adapter = assetImageAdapter
        mBinding.rvKeyFeature.adapter = editTextAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AddProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Add product", true)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            mBinding.edtLocation -> {
                val mArrayList =
                    arrayListOf(
                        "Smartphones",
                        "Laptops",
                        "Fragrances",
                        "Skincare",
                        "Groceries",
                        "Home-Decoration",
                        "Furniture",
                        "Tops",
                        "Women's-dresses",
                        "women's-shoes",
                        "Mens-shirts",
                        "Mens-shoes",
                        "Mens-watches",
                        "Women's-watches",
                        "Women's-bags",
                        "Women's-jewellery",
                        "Sunglasses",
                        "Automotive",
                        "Motorcycle",
                        "Lighting"
                    )
                BottomSheetCommonPopup.newInstance(
                    getString(R.string.select_category),
                    mArrayList,
                    object :
                        BottomSheetCommonPopup.BottomSheetItemClickListener {
                        override fun onItemClick(data: String) {
                            mBinding.edtLocation.setText(data)
                        }

                    }).show(this)
            }
            mBinding.btnSubmit -> {
                openHomeActivity()
            }
            mBinding.cardAddImage -> {
                openImageFilePicker(object : FilePickerCallback {
                    override fun onSuccess(media: Media?) {
                        if (media == null) return
                        mAssetImages.add(0, media)
                        editTextAdapter.notifyDataSetChanged()
                    }

                    override fun onSuccess(mediaList: ArrayList<Media>?) {
                        if (mediaList.isNullOrEmpty()) return
                        mAssetImages.addAll(0, mediaList)
                        editTextAdapter.notifyDataSetChanged()
                    }

                    override fun onError(error: String?) {
                        toast(error)
                    }
                })
            }
            mBinding.txtKeyFeature -> {
                editTextAdapter.setItem(0,"" )
                editTextAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onAssetImageClick(media: Media, position: Int) {
        StfalconImageViewer.Builder(this, mAssetImages) { view, image ->
            Glide.with(mBinding.rvPhoto).load(image.url).into(view)
        }.withStartPosition(position).show()
    }

    private fun onAssetImageDelete(media: Media, position: Int) {
        mAssetImages.removeAt(position)
        editTextAdapter.notifyItemRemoved(position)
    }

    private fun onKeyFeature(media: String, position: Int) {
        mKeyFeature.removeAt(position)
        editTextAdapter.notifyItemRemoved(position)
    }
}