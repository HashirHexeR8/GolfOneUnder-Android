package com.golfApp.golfOneUnder

import com.golfApp.golfOneUnder.model.DeviceInfoDTO
import com.golfApp.golfOneUnder.model.MessageEvent
import com.golfApp.golfOneUnder.utls.Constants
import com.google.android.gms.maps.model.LatLng
import com.nutspace.nut.api.BleDeviceConsumer
import com.nutspace.nut.api.BleDeviceManager
import com.nutspace.nut.api.callback.ConnectStateChangedCallback
import com.nutspace.nut.api.model.BleDevice
import org.greenrobot.eventbus.EventBus

class RangeFinderService: ConnectStateChangedCallback {

    companion object {
        val sharedInstance = RangeFinderService()
    }

    private constructor()

    private var connectedDevice: BleDevice? = null
    private var deviceLocation: LatLng? = null

    fun setDeviceLocation(location: LatLng) {
        deviceLocation = location
    }

    fun getConnectedDevice(): BleDevice? {
        return connectedDevice
    }

    fun isConnectedToADevice(): Boolean {
        return connectedDevice != null
    }

    override fun onConnect(device: BleDevice?) {
        connectedDevice = device
        EventBus.getDefault().post(MessageEvent(Constants.eventBusNotifDeviceConnected))
    }

    override fun onDisconnect(device: BleDevice?, error: Int) {
        val deviceInfo = DeviceInfoDTO()
        deviceInfo.deviceName = device?.name ?: ""
        deviceInfo.deviceLatitude = deviceLocation?.latitude ?: 0.0
        deviceInfo.deviceLongitude = deviceLocation?.longitude ?: 0.0
        UserPrefs.getInstance().addUserDevice(deviceInfo)
        connectedDevice = null
    }


}