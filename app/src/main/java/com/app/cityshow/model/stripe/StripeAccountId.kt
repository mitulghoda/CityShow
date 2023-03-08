package com.sellnu.frekis.model.stripe

import android.os.Parcel
import android.os.Parcelable

class StripeAccountId() : Parcelable {
    @JvmField
    var account_id: String? = null

    @JvmField
    var country_code: String? = null

    constructor(parcel: Parcel) : this() {
        account_id = parcel.readString()
        country_code = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(account_id)
        parcel.writeString(country_code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StripeAccountId> {
        override fun createFromParcel(parcel: Parcel): StripeAccountId {
            return StripeAccountId(parcel)
        }

        override fun newArray(size: Int): Array<StripeAccountId?> {
            return arrayOfNulls(size)
        }
    }
}