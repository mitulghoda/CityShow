package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowCategoryBinding
import com.app.cityshow.databinding.RowProductBinding
import com.app.cityshow.model.CategoryModel

class CategoryListAdapter(
    var mArrayList: ArrayList<CategoryModel>,
    var onClickItem: (device: CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
//        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return 10
    }

    companion object {
        class CategoryHolder(
            var binding: RowCategoryBinding,
            var adapter: CategoryListAdapter
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: CategoryModel) {

            }
        }
    }
}