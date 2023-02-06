package com.app.cityshow.model

import android.os.Parcel
import android.os.Parcelable

class CountryModel() : Parcelable {
    val city: String? = null
    val state: String? = null

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(state)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryModel> {
        override fun createFromParcel(parcel: Parcel): CountryModel {
            return CountryModel(parcel)
        }

        override fun newArray(size: Int): Array<CountryModel?> {
            return arrayOfNulls(size)
        }
    }
}