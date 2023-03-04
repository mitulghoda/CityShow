package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowSubscriptionPlanBinding
import com.app.cityshow.model.subscription.Plan

class PlanListAdapter(
    var mArrayList: ArrayList<Plan>,
    var onClickItem: (product: Plan, type: Int) -> Unit,
) : RecyclerView.Adapter<PlanListAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowSubscriptionPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val data = mArrayList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onClickItem.invoke(data, 1)
        }

    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(data: java.util.ArrayList<Plan>) {
        this.mArrayList = data
        notifyDataSetChanged()
    }

    companion object {
        class CategoryHolder(
            var binding: RowSubscriptionPlanBinding,
            var adapter: PlanListAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: Plan) {
                binding.data = product
            }
        }
    }
}