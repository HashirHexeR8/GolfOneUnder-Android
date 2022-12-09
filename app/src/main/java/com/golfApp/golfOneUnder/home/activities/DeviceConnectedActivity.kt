package com.golfApp.golfOneUnder.home.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.golfApp.golfOneUnder.RangeFinderService
import com.golfApp.golfOneUnder.databinding.ActivityDeviceConnectedBinding

class DeviceConnectedActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivityDeviceConnectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityDeviceConnectedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.tvDeviceConnectedName.text = RangeFinderService.sharedInstance.getConnectedDevice()?.name
        viewBinding.btnTrackLocation.setOnClickListener {
            finish()
        }
    }

}