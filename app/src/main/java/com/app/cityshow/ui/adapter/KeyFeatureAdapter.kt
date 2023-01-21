package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RawKeyFeatureBinding

class KeyFeatureAdapter(
    var arrayList: ArrayList<String>
) : RecyclerView.Adapter<KeyFeatureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RawKeyFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    fun setItem(data: List<String>) {
        arrayList = data as ArrayList<String>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawKeyFeatureBinding, private val adapter: KeyFeatureAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(media: String) {
            binding.data = media
        }
    }

}