package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowDisocutBinding
import com.app.cityshow.model.disocunt.Discount

class DiscountsAdapter(
    var mArrayList: ArrayList<Discount>,
    var onClickItem: (device: Discount) -> Unit,
) : RecyclerView.Adapter<DiscountsAdapter.Companion.DiscountHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountHolder {
        val binding =
            RowDisocutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DiscountHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(Discounts: List<Discount>) {
        this.mArrayList.clear()
        mArrayList = Discounts as ArrayList<Discount>
        notifyDataSetChanged()

    }

    companion object {
        class DiscountHolder(
            var binding: RowDisocutBinding,
            var adapter: DiscountsAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(discount: Discount) {
                binding.data = discount
                binding.root.setOnClickListener {
                    adapter.onClickItem.invoke(discount)
                }
            }
        }
    }
}