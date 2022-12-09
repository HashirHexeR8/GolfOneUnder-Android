package com.golfApp.golfOneUnder.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.golfApp.golfOneUnder.databinding.DeviceListRowItemBinding
import com.golfApp.golfOneUnder.model.DeviceInfoDTO
import com.nutspace.nut.api.model.BleDevice

class NotificationsListAdapter(tapDevice: (device: DeviceInfoDTO) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mDataSource: ArrayList<DeviceInfoDTO> = ArrayList()
    private var onTapDevice: ((device: DeviceInfoDTO) -> Unit) = tapDevice

    public fun setDataSource(deviceList: ArrayList<DeviceInfoDTO>) {
        val index = mDataSource.size
        mDataSource = deviceList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemViewBinding = DeviceListRowItemBinding.inflate(LayoutInflater.from(parent.context))
        return NotificationDeviceItemView(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return mDataSource.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationDeviceItemView) {
            holder.setItemInfo(mDataSource[position])
        }
    }

    inner class NotificationDeviceItemView(val itemViewBinding: DeviceListRowItemBinding): RecyclerView.ViewHolder(itemViewBinding.root) {
        fun setItemInfo(device: DeviceInfoDTO) {
            itemViewBinding.btnConnect.text = "View Location"
            itemViewBinding.tvDeviceName.text = device.deviceName
            itemViewBinding.btnConnect.setOnClickListener {
                onTapDevice.invoke(device)
            }
        }
    }
}