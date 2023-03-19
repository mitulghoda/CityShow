package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowSubCategoryBinding
import com.app.cityshow.model.category.SubCategory

class BottomSheetSubCategoryAdapter(
    private var mArrayList: ArrayList<SubCategory>,
    private val onItemClickCallback: (data: SubCategory) -> Unit,
) :
    RecyclerView.Adapter<BottomSheetSubCategoryAdapter.Companion.DataViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RowSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            var binding: RowSubCategoryBinding,
            var adapter: BottomSheetSubCategoryAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: SubCategory) {
                binding.txtEpc.text = data.name
                binding.root.setOnClickListener {
                    adapter.onItemClickCallback.invoke(data)
                }
            }
        }
    }
}