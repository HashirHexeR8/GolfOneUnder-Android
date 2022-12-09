package com.golfApp.golfOneUnder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.golfApp.golfOneUnder.databinding.ActivitySplashBinding
import com.golfApp.golfOneUnder.home.activities.HomeActivity
import com.golfApp.golfOneUnder.onboarding.activities.SignInActivity


class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Handler().postDelayed(Runnable { /* Create an Intent that will start the Menu-Activity. */
            var mainIntent = Intent(this@SplashActivity, HomeActivity::class.java)
            if (UserPrefs.getInstance().getUserId().isEmpty() && UserPrefs.getInstance().getUserAuthToken().isEmpty()) {
                mainIntent = Intent(this@SplashActivity, SignInActivity::class.java)
            }
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
        }, 2000)
    }

}