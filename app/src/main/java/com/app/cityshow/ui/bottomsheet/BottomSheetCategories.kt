package com.app.cityshow.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.FragmentActivity
import com.app.cityshow.R
import com.app.cityshow.databinding.BottomSheetRecyclerviewBinding
import com.app.cityshow.model.categoryMode.Category
import com.app.cityshow.ui.adapter.BottomSheetCommonAdapter
import com.app.cityshow.utility.decorator.DividerItemDecorator
import java.util.ArrayList

class BottomSheetCategories : BaseHeaderBottomSheet() {
    private lateinit var binding: BottomSheetRecyclerviewBinding
    private var clickListener: BottomSheetItemClickListener? = null
    private var adapter: BottomSheetCommonAdapter? = null
    private var mArrayList = ArrayList<Category>()
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomSheetRecyclerviewBinding.inflate(inflater, container, false)
        headerView = binding.headerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeTitle(title)
        initUi()
    }

    private fun initUi() {

        binding.recyclerView.addItemDecoration(
            DividerItemDecorator(getDrawable(requireContext(), R.drawable.divider_decorator_gray))
        )
        adapter = BottomSheetCommonAdapter(mArrayList) {
            clickListener?.onItemClick(it)
            dismiss()
        }
        binding.recyclerView.adapter = adapter
    }

    override fun show(activity: FragmentActivity): BaseBottomSheet? {
        return super.show(activity, BottomSheetCategories::class.java.simpleName)
    }

    companion object {
        fun newInstance(
            title: String,
            mArrayList: ArrayList<Category>,
            itemClickListenerListener: BottomSheetItemClickListener,
        ): BottomSheetCategories {
            val fragment = BottomSheetCategories()
            fragment.title = title
            fragment.mArrayList = mArrayList
            fragment.clickListener = itemClickListenerListener
            return fragment
        }
    }

    interface BottomSheetItemClickListener {
        fun onItemClick(data: Category)
    }
}