package com.golfApp.golfOneUnder.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.golfApp.golfOneUnder.databinding.DeviceListRowItemBinding
import com.golfApp.golfOneUnder.model.DeviceInfoDTO
import com.nutspace.nut.api.model.BleDevice

class DiscoverDeviceAdapter(tapDevice: (device: BleDevice) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mDataSource: ArrayList<BleDevice> = ArrayList()
    private var onTapDevice: ((device: BleDevice) -> Unit) = tapDevice

    public fun addToDataSource(device: BleDevice) {
        val index = mDataSource.size
        mDataSource.add(device)
        notifyItemInserted(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemViewBinding = DeviceListRowItemBinding.inflate(LayoutInflater.from(parent.context))
        return DiscoverDeviceItemView(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return mDataSource.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DiscoverDeviceItemView) {
            holder.setItemInfo(mDataSource[position])
        }
    }

    inner class DiscoverDeviceItemView(val itemViewBinding: DeviceListRowItemBinding): RecyclerView.ViewHolder(itemViewBinding.root) {
        fun setItemInfo(device: BleDevice) {
            itemViewBinding.btnConnect.text = "Connect"

            itemViewBinding.tvDeviceName.text = device.name
            itemViewBinding.btnConnect.setOnClickListener {
                onTapDevice.invoke(device)
            }
        }
    }
}