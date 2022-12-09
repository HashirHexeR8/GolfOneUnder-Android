package com.golfApp.golfOneUnder

import com.golfApp.golfOneUnder.services.NetworkService
import com.golfApp.golfOneUnder.services.UserService

object ServiceLocator {
    fun getNetworkService(): NetworkService = NetworkService()
    fun getUserService(): UserService = UserService()
}