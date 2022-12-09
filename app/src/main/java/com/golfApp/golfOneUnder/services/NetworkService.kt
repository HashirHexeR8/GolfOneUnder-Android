package com.golfApp.golfOneUnder.services

import android.util.Log
import com.golfApp.golfOneUnder.interfaces.CallbackListener
import com.golfApp.golfOneUnder.model.CallbackResponse
import com.golfApp.golfOneUnder.utls.WebUrls
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

interface INetworkService {
    fun post(url: String, params: Any, callbackListener: (result: CallbackResponse<String>) -> Unit)
}

class NetworkService: INetworkService {
    override fun post(url: String, params: Any, callbackListener:  (result: CallbackResponse<String>) -> Unit) {
        val finalUrl = WebUrls.productionBaseURL + url
        val client = OkHttpClient()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = getRequestJSON(params).toRequestBody(JSON)
        val request = Request.Builder()
            .url(finalUrl)
            .post(body)
            .build()

        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string() ?: ""
                callbackListener.invoke(CallbackResponse(dataResult = responseString))
                Log.e("Network Service Success", responseString)
            }

            override fun onFailure(call: Call, e: IOException) {
                callbackListener.invoke(CallbackResponse(errorMessage = "Error From Server"))
                Log.e("Network Service Failure", e.localizedMessage.toString())
            }
        })
    }

    private fun getRequestJSON(params: Any): String {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.toJson(params)
    }
}