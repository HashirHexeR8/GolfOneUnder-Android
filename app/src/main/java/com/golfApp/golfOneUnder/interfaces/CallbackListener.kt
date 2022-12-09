package com.golfApp.golfOneUnder.interfaces

import com.golfApp.golfOneUnder.model.CallbackResponse

interface CallbackListener <T> {
    fun onCallback(callbackResponse: CallbackResponse<T>)
}