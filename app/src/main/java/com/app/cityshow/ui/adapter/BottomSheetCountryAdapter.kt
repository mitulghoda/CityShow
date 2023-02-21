package com.app.cityshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.RowTextviewBinding
import com.app.cityshow.model.CountryModel
import com.app.cityshow.utility.hide

class BottomSheetCountryAdapter(
    private var mArrayList: ArrayList<CountryModel>,
    private val onItemClickCallback: (data: CountryModel) -> Unit,
) :
    RecyclerView.Adapter<BottomSheetCountryAdapter.Companion.DataViewHolder?>(), Filterable {
    var dataListTemp = ArrayList<CountryModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RowTextviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataListTemp[position])
        holder.binding.checkboxName.hide()
    }

    override fun getItemCount(): Int {
        return dataListTemp.size
    }

    companion object {
        class DataViewHolder(
            var binding: RowTextviewBinding,
            var adapter: BottomSheetCountryAdapter,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: CountryModel) {
                binding.txtEpc.text = data.city
                binding.checkboxName.setOnClickListener { adapter.onItemClickCallback.invoke(data) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataListTemp = mArrayList
                } else {
                    val resultList = ArrayList<CountryModel>()
                    for (row in mArrayList) {
                        if (row.city!!.lowercase()
                                .startsWith(constraint!!.toString().lowercase())
                        ) {
                            resultList.add(row)
                        }
                    }
                    dataListTemp = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataListTemp
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataListTemp = results?.values as ArrayList<CountryModel>
                notifyDataSetChanged()
            }
        }
    }

    init {
        dataListTemp = mArrayList
    }

}