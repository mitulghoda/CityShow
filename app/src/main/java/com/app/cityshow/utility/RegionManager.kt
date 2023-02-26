package com.app.cityshow.utility

import android.content.Context
import com.app.cityshow.model.CountryModel
import com.app.cityshow.model.dynamicview.CategoryWiseViewModel
import com.app.cityshow.model.dynamicview.CategoryWiseViewModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream

class RegionManager private constructor() {
    private fun loadCountries(context: Context) {
        countries = Gson().fromJson<List<CountryModel>?>(
            readJsonFromAsset("city_list.json", context),
            object : TypeToken<List<CountryModel?>?>() {}.type
        )
    }

    private fun loadCategoryWise(context: Context) {
        try {
            categoryWiseViewModel = Gson().fromJson<List<CategoryWiseViewModelItem>?>(
                readJsonFromAsset("category_wise_list.json", context),
                object : TypeToken<List<CategoryWiseViewModelItem?>?>() {}.type
            )
        } catch (ex: Exception) {
            Log.e("Exception", ex.message ?: "")
        }

    }

    companion object {
        private var regionManager: RegionManager? = null
        private var countries: List<CountryModel>? = null
        private var categoryWiseViewModel: List<CategoryWiseViewModelItem>? = null
        val instance: RegionManager?
            get() {
                if (regionManager == null) regionManager = RegionManager()
                return regionManager
            }

        fun init(context: Context) {
            val regionManager = instance
            regionManager?.loadCountries(context)
            regionManager?.loadCategoryWise(context)
        }

        fun getCountries(): ArrayList<CountryModel>? {
            return countries?.sortedBy { it.city }?.let { ArrayList(it) }
        }

        fun getCategoryWiseView(): ArrayList<CategoryWiseViewModelItem>? {
            return categoryWiseViewModel as ArrayList<CategoryWiseViewModelItem>?
        }

        fun readJsonFromAsset(filename: String?, context: Context): String? {
            val manager = context.assets
            var file: InputStream? = null
            try {
                file = manager.open(filename!!)
                val formArray = ByteArray(file.available())
                file.read(formArray)
                file.close()
                return String(formArray)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}