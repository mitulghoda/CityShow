package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.ItemAddImageBinding
import com.app.cityshow.databinding.RowEditextBinding
import com.app.cityshow.utility.loadImage
import com.filepickersample.model.Media

class EditTextAdapter : RecyclerView.Adapter<EditTextAdapter.ViewHolder>() {
    var arrayList = ArrayList<String>()
    var deleteCallback: ((media: String, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowEditextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    fun setItem(pos:Int,data: String) {
        arrayList.add(data)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RowEditextBinding, private val adapter: EditTextAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(media: String) {
            binding.ivRemvoe.setOnClickListener {
                adapter.deleteCallback?.invoke(media, adapterPosition)
            }
        }
    }

}