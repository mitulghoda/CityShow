package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowShopNameBinding
import com.app.cityshow.model.product.Product

class BottomSheetProductAdapter(
    private var mArrayList: ArrayList<Product>,
    private val onItemClickCallback: (data: Product) -> Unit,
) : RecyclerView.Adapter<BottomSheetProductAdapter.Companion.DataViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = RowShopNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            var adapter: BottomSheetProductAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Product) {
                binding.txtEpc.text = data.name
                binding.checkboxName.isChecked = data.checked
                binding.linRoot.setOnClickListener {
                    data.checked = data.checked
                    adapter.notifyItemChanged(adapterPosition)
                    adapter.onItemClickCallback.invoke(data)
                }
            }
        }
    }
}