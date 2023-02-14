package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowCategoryBinding
import com.app.cityshow.databinding.RowHomeDiscountBinding
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.utility.DateTimeUtil
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.loadImage
import com.app.cityshow.utility.show

class DiscountListAdapter(
    var mArrayList: ArrayList<Discount>,
    var onClickItem: (device: Discount) -> Unit,
) : RecyclerView.Adapter<DiscountListAdapter.Companion.DiscountHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountHolder {
        val binding =
            RowHomeDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DiscountHolder, position: Int) {
        holder.bind(mArrayList[position])
//        holder.binding.tvSubType.hide()
//        holder.binding.tvDate.show()
        /*2023-01-06 20:23:00*/
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(data: List<Discount>) {
        mArrayList = data as ArrayList<Discount>
        notifyDataSetChanged()
    }

    companion object {
        class DiscountHolder(
            var binding: RowHomeDiscountBinding,
            var adapter: DiscountListAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: Discount) {
                binding.data = reader
                binding.image.loadImage(reader.image)
                binding.root.setOnClickListener {
                    adapter.onClickItem(reader)
                }
            }
        }
    }
}