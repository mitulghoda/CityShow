package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowShopsBinding
import com.app.cityshow.model.CategoryModel

class ShopsAdapter(
    var mArrayList: ArrayList<CategoryModel>,
    var onClickItem: (device: CategoryModel) -> Unit,
) : RecyclerView.Adapter<ShopsAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowShopsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
//        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return 40
    }

    companion object {
        class CategoryHolder(
            var binding: RowShopsBinding,
            var adapter: ShopsAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: CategoryModel) {

            }
        }
    }
}