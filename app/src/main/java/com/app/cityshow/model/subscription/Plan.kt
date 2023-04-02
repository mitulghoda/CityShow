package com.app.cityshow.model.subscription

data class Plan(
    val active: Boolean,
    val attributes: List<Any>,
    val created: Int,
    val default_price: String,
    val description: String,
    val id: String,
    val images: List<Any>,
    val livemode: Boolean,
    val metadata: Metadata,
    val name: String,
    val `object`: String,
    val package_dimensions: Any,
    val price_data: PriceData,
    val shippable: Any,
    val statement_descriptor: Any,
    val tax_code: Any,
    val type: String,
    val unit_label: Any,
    val updated: Int,
    val url: Any,
) {
    fun getPackageDuration(): String {
        return "Rs${price_data.unit_amount / 100} / ${metadata.period}"
    }

    fun getPackageShops(): String {
        return "Allow Max ${metadata.shops} shops"
    }

    fun getPackageProduct(): String {
        return "Allow Max ${metadata.products} products"
    }
    fun getPackagePhoto(): String {
        return "Allow Max ${metadata.photo} photos for one product"
    }
}