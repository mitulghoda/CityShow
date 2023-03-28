package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowCategoryBinding
import com.app.cityshow.model.category.Category
import com.app.cityshow.utility.loadImage

class CategoryListAdapter(
    var mArrayList: ArrayList<Category>,
    var onClickItem: (device: Category) -> Unit,
) : RecyclerView.Adapter<CategoryListAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(mArrayList[position])
//        holder.binding.image.loadImage("data:image/jpeg;base64,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")
    }

    override fun getItemCount(): Int {
        if (mArrayList.size>5){
            return 5
        }
        return mArrayList.size
    }

    fun setData(data: List<Category>?) {
        mArrayList = data as ArrayList<Category>
        notifyDataSetChanged()
    }

    companion object {
        class CategoryHolder(
            var binding: RowCategoryBinding,
            var adapter: CategoryListAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: Category) {
                binding.data = reader
                binding.image.loadImage(reader.getCategoryImage())
                binding.root.setOnClickListener {
                    adapter.onClickItem(reader)
                }
            }
        }
    }
}