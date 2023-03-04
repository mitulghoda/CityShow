package com.app.cityshow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.R
import com.app.cityshow.databinding.RowProductBinding
import com.app.cityshow.model.product.Product
import com.app.cityshow.pagination.PagedAdapter
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.loadImage
import com.app.cityshow.utility.show

class SearchFriendAdapter(
    var context: Context,
    var mArrayList: ArrayList<Product>?,
    var onClickItem: (product: Product, type: Int) -> Unit,
) : PagedAdapter<SearchFriendAdapter.Companion.ViewHolder, Product>(context) {

    override fun getItemViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            this
        )
    }

    override fun onItemBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bind(currentList[position], position)
    }

    override val diffCallback: DiffUtil.ItemCallback<Product>
        get() = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product,
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }

    companion object {
        class ViewHolder(
            val binding: RowProductBinding,
            val adapter: SearchFriendAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Product, position: Int) {
                binding.data = data
                binding.ivProduct.loadImage(
                    data.getProductImage(),
                    R.drawable.ic_logo
                )
                binding.ivFav.isSelected = data.isFavourite()
                if (data.discount != null && !data.discount?.id.isNullOrEmpty()) {
                    binding.tvDiscount.show()
                } else {
                    binding.tvDiscount.hide()
                }
                itemView.setOnClickListener {
                    adapter.onClickItem.invoke(data, 1)
                }
                binding.ivFav.setOnClickListener {
                    data.is_fav = if (data.isFavourite()) "0" else "1"
                    adapter.notifyItemChanged(position)
                    adapter.onClickItem.invoke(data, 0)
                }
            }
        }
    }
}