package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowShopsBinding
import com.app.cityshow.model.shops.Shop

class ShopsAdapter(
    var mArrayList: ArrayList<Shop>,
    var onClickItem: (device: Shop) -> Unit,
) : RecyclerView.Adapter<ShopsAdapter.Companion.ShopHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        val binding =
            RowShopsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(shops: List<Shop>) {
        this.mArrayList.clear()
        mArrayList = shops as ArrayList<Shop>
        notifyDataSetChanged()

    }

    companion object {
        class ShopHolder(
            var binding: RowShopsBinding,
            var adapter: ShopsAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(shop: Shop) {
                binding.data = shop

                binding.root.setOnClickListener {
                    adapter.onClickItem.invoke(shop)
                }
            }
        }
    }
}