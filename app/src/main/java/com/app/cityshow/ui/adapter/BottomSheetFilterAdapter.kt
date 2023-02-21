package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.FilterType
import com.app.cityshow.databinding.RowTextviewBinding
import com.app.cityshow.utility.hide
import java.util.ArrayList
import java.util.logging.Filter

class BottomSheetFilterAdapter(
    private var mArrayList: ArrayList<FilterType>,
    private val onItemClickCallback: (data: FilterType) -> Unit,
) :
    RecyclerView.Adapter<BottomSheetFilterAdapter.Companion.DataViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RowTextviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(mArrayList[position])
        holder.binding.checkboxName.hide()
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    companion object {
        class DataViewHolder(
            var binding: RowTextviewBinding,
            var adapter: BottomSheetFilterAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: FilterType) {
                binding.txtEpc.text = data.strValue
                binding.linRoot.setOnClickListener { adapter.onItemClickCallback.invoke(data) }
            }
        }
    }
}