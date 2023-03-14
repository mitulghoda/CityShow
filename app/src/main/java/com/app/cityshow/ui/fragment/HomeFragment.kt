package com.app.cityshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.app.cityshow.Controller
import com.app.cityshow.FilterType
import com.app.cityshow.R
import com.app.cityshow.databinding.HomeFragmentBinding
import com.app.cityshow.databinding.ItemCustomFixedSizeLayout3Binding
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.model.product.Product
import com.app.cityshow.ui.adapter.CategoryListAdapter
import com.app.cityshow.ui.adapter.ProductListAdapter
import com.app.cityshow.ui.bottomsheet.BottomSheetFilter
import com.app.cityshow.ui.common.BaseFragment
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.typeCall
import com.app.cityshow.viewmodel.ProductViewModel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage

class HomeFragment : BaseFragment(), View.OnClickListener {
    lateinit var categoryListAdapter: CategoryListAdapter
    lateinit var productListAdapter: ProductListAdapter
    private lateinit var binding: HomeFragmentBinding
    private var viewModel: ProductViewModel? = null
    val mArrayList = ArrayList<CategoryModel>()
    val productList = ArrayList<Product>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickListener = this
        initViewModel()
        setAdapter()

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ProductViewModel::class.java]
        callGetCategoryApi()
        calGetProducts("")
        callGetMyDiscounts()
    }

    private fun callGetMyDiscounts() {
        val param = HashMap<String, Any>()
        viewModel?.myDiscounts(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    if (it.data != null && it.data.success) {
                        autoScrollRecyclerView(it.data.data.discounts)
                    } else {
                        binding.layoutDiscount.hide()
                    }
                },
                error = {
                }, loading = {
                })
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun callGetCategoryApi() {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["pagination"] = "false"
        param["page"] = "1"
        param["limit"] = "10000"
        viewModel?.getCategories(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        categoryListAdapter.setData(it.data.data.categories)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage(it.message)
                }, loading = {})
        }
//            } else {
//                hideProgressDialog()
//                toast(fcmToken)
//            }
    }

    private fun calGetProducts(strFilter: String) {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["filter"] = strFilter
        param["city"] = base?.city.toString()
        param["latitude"] = base?.lattitude.toString()
        param["longitude"] = base?.longitude.toString()
        param["pagination"] = "false"
        param["limit"] = "30"
        param["page"] = "1"
        viewModel?.listOfProduct(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        val list = it.data.data.products
                        productListAdapter.setData(list)
                    } else {
                        base?.showAlertMessage(it.message)
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage(it.message)
                }, loading = { base?.showProgressDialog() })
        }

    }

    private fun setAdapter() {
        categoryListAdapter = CategoryListAdapter(arrayListOf()) {
            navigation?.openProductListActivity(it)
        }
        binding.rvCategories.adapter = categoryListAdapter
        productListAdapter = ProductListAdapter(productList) { product, type ->
            when (type) {
                0 -> {
                    markFavProduct(product)
                }
                1 -> {
                    navigation?.openProductDetails(product)
                }
            }
        }
        binding.rvProducts.adapter = productListAdapter
    }

    private fun autoScrollRecyclerView(data: List<Discount>) {
        binding.carousel4.carouselListener = object : CarouselListener {
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                return ItemCustomFixedSizeLayout3Binding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }

            override fun onBindViewHolder(
                binding: ViewBinding,
                item: CarouselItem,
                position: Int
            ) {
                val currentBinding = binding as ItemCustomFixedSizeLayout3Binding
                currentBinding.imageView.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setImage(item, R.drawable.ic_app_logo)
                }
            }

            override fun onClick(position: Int, carouselItem: CarouselItem) {
                super.onClick(position, carouselItem)
                navigation?.openDiscountProductListActivity(data[position])
            }
        }

        val listFour = mutableListOf<CarouselItem>()
        data.forEach { discount ->
            listFour.add(
                CarouselItem(
                    imageUrl = discount.image
                )
            )
        }
        binding.carousel4.setData(listFour)
    }


    private fun markFavProduct(device: Product) {
        base?.showProgressDialog()
        val param = HashMap<String, Any>()
        param["product_id"] = device.id ?: ""
        viewModel?.markFav(param)?.observe(viewLifecycleOwner) {
            it.status.typeCall(
                success = {
                    base?.hideProgressDialog()
                    if (it.data != null && it.data.success) {
                        base?.toast(it.data.message)
                    } else {
                        base?.showAlertMessage(
                            "",
                            it.data?.message ?: getString(R.string.something_went_wrong)
                        )
                    }
                }, error = {
                    base?.hideProgressDialog()
                    base?.showAlertMessage("", it.message)
                }, loading = {})
        }

    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivFilter -> {
                BottomSheetFilter.newInstance(getString(R.string.filter),
                    arrayListOf(),
                    object : BottomSheetFilter.BottomSheetItemClickListener {
                        override fun onItemClick(data: FilterType) {
                            calGetProducts(data.type.toString())
                            binding.txtTrending.text = data.strValue
                        }
                    }).show(base!!)
            }
        }
    }
}