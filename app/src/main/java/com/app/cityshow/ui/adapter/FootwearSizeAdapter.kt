package com.app.cityshow.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RawFootwearSizeBinding
import com.app.cityshow.model.FootwearSizeModel

class FootwearSizeAdapter(
    var list: ArrayList<FootwearSizeModel>,
    var listener: (Int, Int, FootwearSizeModel) -> Unit,
) :
    RecyclerView.Adapter<FootwearSizeAdapter.ViewHolder>() {

    fun setData(data: ArrayList<FootwearSizeModel>) {
        list = data
        notifyDataSetChanged()
    }

    class ViewHolder(
        val binding: RawFootwearSizeBinding,
        private val adapter: FootwearSizeAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun bind(data: FootwearSizeModel) {

            binding.txtName.text = data.name
            binding.checkboxName.isChecked = data.isCheck

            binding.mainLayout.setOnClickListener {
                data.isCheck = !data.isCheck
                adapter.listener.invoke(it.id, layoutPosition, data)
            }
            binding.checkboxName.setOnClickListener {
                data.isCheck = !data.isCheck
                adapter.listener.invoke(it.id, layoutPosition, data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            RawFootwearSizeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}