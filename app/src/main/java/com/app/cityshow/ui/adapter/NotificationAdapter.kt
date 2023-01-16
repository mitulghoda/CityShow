package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowNotificationBinding
import com.app.cityshow.model.categoryMode.CategoryModel

class NotificationAdapter(
    var mArrayList: ArrayList<CategoryModel>,
    var onClickItem: (device: CategoryModel) -> Unit,
) : RecyclerView.Adapter<NotificationAdapter.Companion.CategoryHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
//        holder.bind(mArrayList[position])
    }

    override fun getItemCount(): Int {
        return 40
    }

    companion object {
        class CategoryHolder(
            var binding: RowNotificationBinding,
            var adapter: NotificationAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: CategoryModel) {

            }
        }
    }
}