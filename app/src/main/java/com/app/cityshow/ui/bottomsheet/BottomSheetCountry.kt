package com.app.cityshow.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.FragmentActivity
import com.app.cityshow.R
import com.app.cityshow.databinding.BottomSheetRecyclerviewBinding
import com.app.cityshow.model.CityModel
import com.app.cityshow.ui.adapter.BottomSheetCountryAdapter
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.decorator.DividerItemDecorator
import com.app.cityshow.utility.gson
import com.app.cityshow.utility.onTextChanged
import com.app.cityshow.utility.show

class BottomSheetCountry : BaseHeaderBottomSheet() {
    private lateinit var binding: BottomSheetRecyclerviewBinding
    private var clickListener: BottomSheetItemClickListener? = null
    private var adapter: BottomSheetCountryAdapter? = null
    private var mArrayList = ArrayList<CityModel>()
    private var selectedCities = ArrayList<String>()
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
        setExpanded(true)
        changeTitle(title)
        initUi()
    }

    private fun initUi() {
        binding.clickListener = this
        binding.btnSubmit.show()
        binding.edtSearch.show()
        binding.recyclerView.addItemDecoration(
            DividerItemDecorator(getDrawable(requireContext(), R.drawable.divider_decorator_gray))
        )
        binding.edtSearch.onTextChanged { s, start, before, count ->
            if (s.isNullOrEmpty()) {
                adapter?.filter?.filter("")
            } else adapter?.filter?.filter(s)
        }

        adapter = BottomSheetCountryAdapter(mArrayList) {
            if (selectedCities.isEmpty()) {
                selectedCities.add(it.name!!)
            } else {
                if (selectedCities.contains(it.name)) {
                    selectedCities.remove(it.name)
                } else {
                    selectedCities.add(it.name!!)
                }
            }
        }
        binding.recyclerView.adapter = adapter
    }

    override fun show(activity: FragmentActivity): BaseBottomSheet? {
        return super.show(activity, BottomSheetCountry::class.java.simpleName)
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view) {
            binding.btnSubmit -> {
                Log.e("SELECTED_CITIES", gson.toJson(selectedCities))
                clickListener?.onItemClick(selectedCities)
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(
            title: String,
            mArrayList: ArrayList<CityModel>,
            itemClickListenerListener: BottomSheetItemClickListener,
        ): BottomSheetCountry {
            val fragment = BottomSheetCountry()
            fragment.title = title
            fragment.mArrayList = mArrayList
            fragment.clickListener = itemClickListenerListener
            return fragment
        }
    }

    interface BottomSheetItemClickListener {
        fun onItemClick(data: ArrayList<String>)
    }
}