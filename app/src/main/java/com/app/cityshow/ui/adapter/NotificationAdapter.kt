package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowNotificationBinding
import com.app.cityshow.model.Notification

class NotificationAdapter(
    var mArrayList: ArrayList<Notification>,
    var onClickItem: (device: Notification) -> Unit,
) : RecyclerView.Adapter<NotificationAdapter.Companion.NotificationHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding, this)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(mArrayList[position])
        holder.binding.notification = mArrayList[position]
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun setData(list: java.util.ArrayList<Notification>) {
        mArrayList = list
        notifyDataSetChanged()

    }

    companion object {
        class NotificationHolder(
            var binding: RowNotificationBinding,
            var adapter: NotificationAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(reader: Notification) {

            }
        }
    }
}