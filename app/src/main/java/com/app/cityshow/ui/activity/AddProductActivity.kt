package com.app.cityshow.ui.activity

import android.os.Bundle
import android.view.View
import com.app.cityshow.R
import com.app.cityshow.databinding.AddProductBinding
import com.app.cityshow.ui.adapter.ImageAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetCommonPopup
import com.bumptech.glide.Glide
import com.filepickersample.listener.FilePickerCallback
import com.filepickersample.model.Media
import com.app.cityshow.ui.common.ActionBarActivity
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

class AddProductActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var mBinding: AddProductBinding
    private var mAssetImages = ArrayList<Media>()
    var assetImageAdapter = ImageAdapter()
    override fun initUi() {
        assetImageAdapter.arrayList = mAssetImages
        assetImageAdapter.clickCallback = this::onAssetImageClick
        assetImageAdapter.deleteCallback = this::onAssetImageDelete
        mBinding.rvPhoto.adapter = assetImageAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AddProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.clickListener = this
        setUpToolbar("Add product", true)
    }

    override fun onClick(v: View?) {
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
                        assetImageAdapter.notifyDataSetChanged()
                    }

                    override fun onSuccess(mediaList: ArrayList<Media>?) {
                        if (mediaList.isNullOrEmpty()) return
                        mAssetImages.addAll(0, mediaList)
                        assetImageAdapter.notifyDataSetChanged()
                    }

                    override fun onError(error: String?) {
                        toast(error)
                    }
                })
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
        assetImageAdapter.notifyItemRemoved(position)
    }
}