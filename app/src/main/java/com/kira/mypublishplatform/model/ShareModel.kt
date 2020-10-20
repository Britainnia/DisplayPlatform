package com.kira.mypublishplatform.model

import android.os.Parcel
import android.os.Parcelable


data class ShareModel(
    var name: String?, var icon: String?, var href: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(href)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShareModel> {
        override fun createFromParcel(parcel: Parcel): ShareModel {
            return ShareModel(parcel)
        }

        override fun newArray(size: Int): Array<ShareModel?> {
            return arrayOfNulls(size)
        }
    }

}