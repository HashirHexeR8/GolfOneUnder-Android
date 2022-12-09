package com.golfApp.golfOneUnder.onboarding.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.golfApp.golfOneUnder.ServiceLocator
import com.golfApp.golfOneUnder.databinding.ActivitySignupBinding
import com.golfApp.golfOneUnder.home.activities.HomeActivity
import com.golfApp.golfOneUnder.model.UserSignUpRequestDTO
import com.golfApp.golfOneUnder.uiUtils.PromptDialog
import com.golfApp.golfOneUnder.utls.Enum


class SignUpActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivitySignupBinding
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnSignUp.setOnClickListener {
            if (validateFields()) {
                if (viewBinding.edSignUpPassword.text.toString() != viewBinding.edSignUpConfirmPassword.text.toString()) {
                    PromptDialog.instance.showAlert(this@SignUpActivity, "Both password fields doesn't match.")
                    return@setOnClickListener
                }
                val dialog = PromptDialog.instance.showProgressDialog(this@SignUpActivity, "Signing In, Please wait.")
                val signupDTO = UserSignUpRequestDTO()
                signupDTO.email = viewBinding.edSignUpEmail.text.toString()
                signupDTO.name = viewBinding.edSignUpFullName.text.toString()
                signupDTO.username = viewBinding.edSignUpUsername.text.toString()
                signupDTO.password = viewBinding.edSignUpPassword.text.toString()

                ServiceLocator.getUserService().signUpUser(signupDTO) { callResponse ->
                    dialog.dismiss()
                    if (callResponse.responseStatus == Enum.responseStatusType.success) {
                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                    else {
                        PromptDialog.instance.showAlert(this@SignUpActivity, "Unable to signup at this moment.")
                    }
                }
            }
            else {
                PromptDialog.instance.showAlert(this@SignUpActivity, "Please fill all fields.")
            }
        }

        viewBinding.btnSignUpPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                viewBinding.edSignUpPassword.transformationMethod = null
            }
            else {
                viewBinding.edSignUpPassword.transformationMethod = PasswordTransformationMethod()
            }
        }

        viewBinding.btnSignUpConfirmPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                viewBinding.edSignUpConfirmPassword.transformationMethod = null
            }
            else {
                viewBinding.edSignUpConfirmPassword.transformationMethod = PasswordTransformationMethod()
            }
        }

        viewBinding.tvSignIn.setOnClickListener {
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (viewBinding.edSignUpEmail.text.isEmpty()) {
            return false
        }
        if (viewBinding.edSignUpFullName.text.isEmpty()) {
            return false
        }
        if (viewBinding.edSignUpUsername.text.isEmpty()) {
            return false
        }
        if (viewBinding.edSignUpPassword.text.isEmpty()) {
            return false
        }
        if (viewBinding.edSignUpConfirmPassword.text.isEmpty()) {
            return false
        }
        return true
    }
}