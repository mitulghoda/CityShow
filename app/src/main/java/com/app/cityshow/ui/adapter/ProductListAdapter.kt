package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.R
import com.app.cityshow.databinding.RowProductBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.utility.loadImage

class ProductListAdapter(
    var mArrayList: ArrayList<Product>,
    var onClickItem: (device: Product) -> Unit,
) : RecyclerView.Adapter<ProductListAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val data = mArrayList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onClickItem.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(data: java.util.ArrayList<Product>) {
        this.mArrayList = data
        notifyDataSetChanged()
    }

    companion object {
        class CategoryHolder(
            var binding: RowProductBinding,
            var adapter: ProductListAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: Product) {
                binding.data = product
                binding.ivProduct.loadImage(product.getCategoryImage(),
                    R.drawable.ic_logo)
            }
        }
    }
}