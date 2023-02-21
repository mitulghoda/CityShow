package com.app.cityshow

enum class FilterType(val strValue: String, val type: Int) {
    LOW_TO_HIGH("Price Low to High", 1),
    HIGH_TO_LOW("Price High to Low", 2),
    NEW_COLLECTION("New Collection", 3),
//    MOST_LIKED("Most Liked", 4),
    TRENDING("Trending", 4)
}