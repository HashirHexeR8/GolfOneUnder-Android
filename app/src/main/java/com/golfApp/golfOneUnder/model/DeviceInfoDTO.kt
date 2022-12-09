package com.golfApp.golfOneUnder.model

import android.os.Parcel
import android.os.Parcelable


class DeviceInfoDTO() : Parcelable {
    var deviceName: String = ""
    var deviceLatitude: Double = 0.0
    var deviceLongitude: Double = 0.0

    constructor(parcel: Parcel) : this() {
        deviceName = parcel.readString() ?: ""
        deviceLatitude = parcel.readDouble()
        deviceLongitude = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deviceName)
        parcel.writeDouble(deviceLatitude)
        parcel.writeDouble(deviceLongitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceInfoDTO> {
        override fun createFromParcel(parcel: Parcel): DeviceInfoDTO {
            return DeviceInfoDTO(parcel)
        }

        override fun newArray(size: Int): Array<DeviceInfoDTO?> {
            return arrayOfNulls(size)
        }
    }
}