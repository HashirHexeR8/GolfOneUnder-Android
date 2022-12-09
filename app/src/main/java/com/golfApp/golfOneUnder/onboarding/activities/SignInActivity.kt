package com.golfApp.golfOneUnder.onboarding.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.golfApp.golfOneUnder.ServiceLocator
import com.golfApp.golfOneUnder.databinding.ActivityMainBinding
import com.golfApp.golfOneUnder.home.activities.HomeActivity
import com.golfApp.golfOneUnder.uiUtils.PromptDialog
import com.golfApp.golfOneUnder.utls.Enum

class SignInActivity: AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogin.setOnClickListener {
            if (validateFields()) {
                val dialog = PromptDialog.instance.showProgressDialog(this@SignInActivity, "Signing In, Please wait.")
                ServiceLocator.getUserService().loginUser(viewBinding.edSignInEmail.text.toString(), viewBinding.edSignInPassword.text.toString()) { callResponse ->
                    dialog.dismiss()
                    if (callResponse.responseStatus == Enum.responseStatusType.success) {
                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        finish()
                        startActivity(intent)                    }
                    else {
                        PromptDialog.instance.showAlert(this@SignInActivity, "Unable to signup at this moment.")
                    }
                }
            }
            else {
                PromptDialog.instance.showAlert(this@SignInActivity, "Please fill all fields.")
            }
        }

        viewBinding.tvForgotPassword.setOnClickListener {
            PromptDialog.instance.showAlert(this@SignInActivity, "This feature is under development.")
        }

        viewBinding.tvSignup.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        viewBinding.btnSignInPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                viewBinding.edSignInPassword.transformationMethod = null
            }
            else {
                viewBinding.edSignInPassword.transformationMethod = PasswordTransformationMethod()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (viewBinding.edSignInEmail.text.isEmpty()) {
            return false
        }
        if (viewBinding.edSignInPassword.text.isEmpty()) {
            return false
        }
        return true
    }
}