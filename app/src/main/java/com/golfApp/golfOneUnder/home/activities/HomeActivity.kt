package com.golfApp.golfOneUnder.home.activities

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.golfApp.golfOneUnder.RangeFinderService
import com.golfApp.golfOneUnder.databinding.ActivityHomeBinding
import com.golfApp.golfOneUnder.model.DeviceInfoDTO
import com.golfApp.golfOneUnder.uiUtils.PromptDialog
import com.golfApp.golfOneUnder.utls.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nutspace.nut.api.BleDeviceManager

class HomeActivity: AppCompatActivity() {

    private var position = LatLng(-33.920455, 18.466941)
    private lateinit var homeViewBinding: ActivityHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isDeviceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        homeViewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeViewBinding.root)

        with(homeViewBinding.mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }

        val launchDiscoverDeviceForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                if (RangeFinderService.sharedInstance.isConnectedToADevice()) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            isDeviceConnected = true
                            homeViewBinding.mapView.visibility = View.VISIBLE
                            homeViewBinding.rlHomeDeviceInfo.visibility = View.VISIBLE
                            homeViewBinding.tvHomeConnectedDeviceName.text = RangeFinderService.sharedInstance.getConnectedDevice()?.name
                            homeViewBinding.llDeviceNotConnected.visibility = View.GONE
                            homeViewBinding.btnHomeConnectDevice.text = "Disconnect Device"
                            position = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                            setupMap()
                        }
                    }
                }
            }
        }

        val startNotificationsForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {  result ->
            if (result.resultCode == RESULT_OK) {
                val deviceInfo = result.data?.getParcelableExtra<DeviceInfoDTO>(Constants.deviceInfoIntentName)
                position = LatLng(deviceInfo?.deviceLatitude ?: 0.0, deviceInfo?.deviceLongitude ?: 0.0)
                setupMap()
            }

        }

        homeViewBinding.rlHomeDeviceInfo.visibility = View.GONE
        homeViewBinding.mapView.visibility = View.GONE
        homeViewBinding.llDeviceNotConnected.visibility = View.VISIBLE

        homeViewBinding.btnHomeConnectDevice.setOnClickListener {
            if (!isDeviceConnected) {
                val intent = Intent(this@HomeActivity, DiscoverDevicesActivity::class.java)
                launchDiscoverDeviceForResult.launch(intent)
            }
            else {
                isDeviceConnected = false
                homeViewBinding.mapView.visibility = View.GONE
                homeViewBinding.rlHomeDeviceInfo.visibility = View.GONE
                homeViewBinding.llDeviceNotConnected.visibility = View.VISIBLE
                homeViewBinding.btnHomeConnectDevice.text = "Connect Device"
                val intent = Intent(this@HomeActivity, DeviceDisconnectedActivity::class.java)
                startActivity(intent)
            }
        }

        homeViewBinding.btnNotifications.setOnClickListener {
            val intent = Intent(this@HomeActivity, NotificationsActivity::class.java)
            startNotificationsForResult.launch(intent)
        }

        checkForPermissions()
    }

    private fun checkForPermissions() {

        val permissionArray: ArrayList<String> = ArrayList()

        if (ContextCompat.checkSelfPermission(this@HomeActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this@HomeActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this@HomeActivity, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this@HomeActivity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ) {
            permissionArray.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionArray.isNotEmpty()) {
            PromptDialog.instance.showAlert(this@HomeActivity, "We require location and bluetooth permissions to discover nearby devices.") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        this,
                        permissionArray.toTypedArray(),
                        99
                    )
                }
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (!mBluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    mBluetoothAdapter.enable()
                }
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

    private fun setupMap() {
        homeViewBinding.llDeviceNotConnected.visibility = View.GONE
        homeViewBinding.mapView.visibility = View.VISIBLE
        with(homeViewBinding.mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        homeViewBinding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        homeViewBinding.mapView.onLowMemory()
    }
}