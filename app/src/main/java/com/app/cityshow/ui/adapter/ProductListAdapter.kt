package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowProductBinding
import com.app.cityshow.model.CategoryModel

class ProductListAdapter(
    var mArrayList: ArrayList<CategoryModel>,
    var onClickItem: (device: CategoryModel) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
//        holder.bind(mArrayList[position])
        holder.itemView.setOnClickListener {
            onClickItem.invoke(CategoryModel())
        }
    }

    override fun getItemCount(): Int {
        return 40
    }

    companion object {
        class CategoryHolder(
            var binding: RowProductBinding,
            var adapter: ProductListAdapter
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: CategoryModel) {

            }
        }
    }
}