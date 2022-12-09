package com.golfApp.golfOneUnder.home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.golfApp.golfOneUnder.databinding.ActivityNotificationsBinding
import com.golfApp.golfOneUnder.home.adapters.DiscoverDeviceAdapter
import com.golfApp.golfOneUnder.home.adapters.NotificationsListAdapter

class NotificationsActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivityNotificationsBinding
    private lateinit var mAdapter: NotificationsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnNotifBack.setOnClickListener {
            finish()
        }

        mAdapter = NotificationsListAdapter {
            setResult(RESULT_OK)
            finish()
        }
        viewBinding.rvNotifDeviceList.layoutManager = LinearLayoutManager(this)
        viewBinding.rvNotifDeviceList.adapter = mAdapter

        mAdapter.setDataSource(UserPrefs.getInstance().getDeviceList())
    }
}