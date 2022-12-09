package com.golfApp.golfOneUnder.model

import com.golfApp.golfOneUnder.utls.Enum

class CallbackResponse<T> {

    constructor(errorMessage: String) {
        this.errorMessage = errorMessage
        this.responseStatus = Enum.responseStatusType.failure
        this.dataResult = null
    }

    constructor(dataResult: T) {
        this.errorMessage = ""
        this.responseStatus = Enum.responseStatusType.success
        this.dataResult = dataResult
    }

    var errorMessage: String
    var dataResult: T?
    var responseStatus: Enum.responseStatusType
}

class BaseNetworkResponse {
    var action: String = ""
    var error: String? = null
}