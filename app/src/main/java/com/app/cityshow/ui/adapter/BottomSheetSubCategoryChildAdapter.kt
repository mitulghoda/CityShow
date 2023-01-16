package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowSubCategoryChildBinding
import com.app.cityshow.model.categoryMode.SubCategory

class BottomSheetSubCategoryChildAdapter(
    private var mArrayList: List<SubCategory>,
    private val onItemClickCallback: (data: SubCategory) -> Unit,
) :
    RecyclerView.Adapter<BottomSheetSubCategoryChildAdapter.Companion.DataViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RowSubCategoryChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            var binding: RowSubCategoryChildBinding,
            var adapter: BottomSheetSubCategoryChildAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: SubCategory) {
                binding.txtEpc.text = data.name
                binding.linRoot.setOnClickListener { adapter.onItemClickCallback.invoke(data) }
            }
        }
    }
}