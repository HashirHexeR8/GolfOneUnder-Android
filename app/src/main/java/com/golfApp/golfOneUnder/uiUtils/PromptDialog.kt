package com.golfApp.golfOneUnder.uiUtils

import android.content.DialogInterface
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class PromptDialog {
    companion object {
        val instance = PromptDialog()
    }

    fun showProgressDialog(activity: AppCompatActivity, message: String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(activity)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(activity)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(activity)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setCancelable(true)
        builder.setView(ll)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        val window: Window? = dialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }

        return dialog
    }

    fun showAlert(activity: AppCompatActivity, message: String) {
        activity.runOnUiThread {
            val alertDialog: androidx.appcompat.app.AlertDialog = androidx.appcompat.app.AlertDialog.Builder(activity).create()
            alertDialog.setTitle("One Under Golf")
            alertDialog.setMessage(message)
            alertDialog.setButton(
                androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            alertDialog.show()
        }
    }

    fun showAlert(activity: AppCompatActivity, message: String, onAlertDismiss: () -> Unit ) {
        activity.runOnUiThread {
            val alertDialog: androidx.appcompat.app.AlertDialog = androidx.appcompat.app.AlertDialog.Builder(activity).create()
            alertDialog.setTitle("One Under Golf")
            alertDialog.setMessage(message)
            alertDialog.setButton(
                androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    onAlertDismiss.invoke()
                })
            alertDialog.show()
        }
    }
}