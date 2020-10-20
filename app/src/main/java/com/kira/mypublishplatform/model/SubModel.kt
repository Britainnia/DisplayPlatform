package com.kira.mypublishplatform.model

import android.os.Parcel
import android.os.Parcelable


data class SubModel(
    var name: String?, var icon: String?, var href: String?, var enable: Boolean?, var needLogin: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(href)
        parcel.writeValue(enable)
        parcel.writeValue(needLogin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubModel> {
        override fun createFromParcel(parcel: Parcel): SubModel {
            return SubModel(parcel)
        }

        override fun newArray(size: Int): Array<SubModel?> {
            return arrayOfNulls(size)
        }
    }

}