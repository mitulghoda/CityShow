package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowSubscriptionPlanBinding
import com.app.cityshow.model.subscription.Plan
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.hide
import com.app.cityshow.utility.show

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
        if (data.id.equals(LocalDataHelper.user?.subscription?.plan_stripe_id, true)) {
            holder.binding.layMain.isSelected = true
            holder.binding.tvActivePlan.show()
            holder.binding.layMain.isClickable = false
            holder.binding.layMain.isEnabled = false
        } else {
            holder.binding.tvActivePlan.hide()
            holder.binding.layMain.isClickable = true
            holder.binding.layMain.isSelected = false
            holder.binding.layMain.isEnabled = true
        }
        holder.binding.layMain.setOnClickListener {
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