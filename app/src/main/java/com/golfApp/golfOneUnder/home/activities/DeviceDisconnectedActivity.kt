package com.golfApp.golfOneUnder.home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.golfApp.golfOneUnder.databinding.ActivityDeviceConnectedBinding
import com.golfApp.golfOneUnder.databinding.ActivityDeviceDisconnectedBinding

class DeviceDisconnectedActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivityDeviceDisconnectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDeviceDisconnectedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnConnectDevice.setOnClickListener {
            finish()
        }
    }
}