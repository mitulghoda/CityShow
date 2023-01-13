package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowShopNameBinding
import com.app.cityshow.model.shops.Shop

class BottomSheetShopAdapter(
    private var mArrayList: ArrayList<Shop>,
    private val onItemClickCallback: (data: Shop) -> Unit,
) :
    RecyclerView.Adapter<BottomSheetShopAdapter.Companion.DataViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RowShopNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    companion object {
        class DataViewHolder(
            var binding: RowShopNameBinding,
            var adapter: BottomSheetShopAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Shop) {
                binding.shops=data
                binding.linRoot.setOnClickListener { adapter.onItemClickCallback.invoke(data) }
            }
        }
    }
}