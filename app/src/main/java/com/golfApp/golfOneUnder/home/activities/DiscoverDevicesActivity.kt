package com.golfApp.golfOneUnder.home.activities

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.golfApp.golfOneUnder.RangeFinderService
import com.golfApp.golfOneUnder.databinding.ActivityDiscoverDevicesBinding
import com.golfApp.golfOneUnder.home.adapters.DiscoverDeviceAdapter
import com.golfApp.golfOneUnder.model.MessageEvent
import com.golfApp.golfOneUnder.uiUtils.PromptDialog
import com.golfApp.golfOneUnder.utls.Constants
import com.google.android.gms.maps.model.LatLng
import com.nutspace.nut.api.BleDeviceConsumer
import com.nutspace.nut.api.BleDeviceManager
import com.nutspace.nut.api.callback.ConnectStateChangedCallback
import com.nutspace.nut.api.callback.EventCallback
import com.nutspace.nut.api.callback.ScanResultCallback
import com.nutspace.nut.api.model.BleDevice
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DiscoverDevicesActivity: AppCompatActivity(), BleDeviceConsumer, ScanResultCallback{

    private lateinit var viewBinding: ActivityDiscoverDevicesBinding
    private lateinit var mAdapter: DiscoverDeviceAdapter

    private var mManager: BleDeviceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForPermissions()
        viewBinding = ActivityDiscoverDevicesBinding.inflate(layoutInflater)
        mManager = BleDeviceManager.getInstance(this)
        setContentView(viewBinding.root)
        mAdapter = DiscoverDeviceAdapter {
            mManager?.connect(this@DiscoverDevicesActivity, it, false)
        }
        viewBinding.rlDiscoverDevices.layoutManager = LinearLayoutManager(this)
        viewBinding.rlDiscoverDevices.adapter = mAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.eventType == Constants.eventBusNotifDeviceConnected) {
            setResult(RESULT_OK)
            finish()
            val intent = Intent(this@DiscoverDevicesActivity, DeviceConnectedActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkForPermissions() {

        val permissionArray: ArrayList<String> = ArrayList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this@DiscoverDevicesActivity, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this@DiscoverDevicesActivity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionArray.isNotEmpty()) {
            PromptDialog.instance.showAlert(this@DiscoverDevicesActivity, "We require bluetooth permissions to discover nearby devices.") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        this,
                        permissionArray.toTypedArray(),
                        99
                    )
                }
            }
        }

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                mBluetoothAdapter.enable()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkForPermissions()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && (permissions.contains(Manifest.permission.BLUETOOTH_SCAN) || permissions.contains(Manifest.permission.BLUETOOTH_CONNECT))) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (!mBluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    mBluetoothAdapter.enable()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mManager?.addScanResultCallback(this)
        mManager?.startScan()
        mManager?.addConnectStateChangedCallback(RangeFinderService.sharedInstance)
        mManager?.bind(this)
    }

    override fun onStop() {
        mManager?.unbind(this)
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onServiceBound() {

    }

    override fun onBleDeviceScanned(device: BleDevice?) {
        mAdapter.addToDataSource(device!!)
    }


}