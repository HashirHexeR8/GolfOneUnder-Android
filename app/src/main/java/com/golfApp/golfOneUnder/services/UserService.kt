package com.golfApp.golfOneUnder.services

import android.util.Log
import com.golfApp.golfOneUnder.ServiceLocator
import com.golfApp.golfOneUnder.interfaces.CallbackListener
import com.golfApp.golfOneUnder.model.*
import com.golfApp.golfOneUnder.utls.Enum
import com.golfApp.golfOneUnder.utls.WebUrls
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

interface IUserService {
    fun loginUser(userEmail: String, password: String, onResponse: (result: CallbackResponse<UserLoginResponseDTO>) -> Unit)
    fun signUpUser(userInfo: UserSignUpRequestDTO, onResponse: (result: CallbackResponse<UserSignUpResponseDTO>) -> Unit)
}
class UserService: IUserService {
    private var mGson: Gson
    init {
        mGson = GsonBuilder().disableHtmlEscaping().create()
    }
    override fun loginUser(userEmail: String, password: String, onResponse: (result: CallbackResponse<UserLoginResponseDTO>) -> Unit) {
        val params: HashMap<String, Any> = HashMap()
        params["email"] = userEmail
        params["password"] = password
        ServiceLocator.getNetworkService().post(WebUrls.loginUserEndPointName, params) { networkCallback ->
            if (networkCallback.responseStatus == Enum.responseStatusType.success) {
                try {
                    var serverResponse = mGson.fromJson(networkCallback.dataResult ?: "", BaseUserLoginResponseDTO::class.java)
                    if (serverResponse.action == "success") {
                        UserPrefs.getInstance().putUserId(serverResponse.data?.id ?: "")
                        UserPrefs.getInstance().putUserToken(serverResponse.data?.token ?: "")
                        UserPrefs.getInstance().putUserInfo(serverResponse.data ?: UserLoginResponseDTO())
                        onResponse.invoke(CallbackResponse(dataResult = serverResponse.data ?: UserLoginResponseDTO()))
                    }
                    else {
                        onResponse.invoke(CallbackResponse(errorMessage = serverResponse.error ?: "Unable to login at this moment." ))
                        Log.e("API Error", serverResponse.error ?: "")
                    }
                }
                catch (exception: Exception) {
                    Log.e("Response Parsing Error", exception.localizedMessage)
                }
            }
            else {
                onResponse.invoke(CallbackResponse(errorMessage = networkCallback.errorMessage))
            }
        }
    }

    override fun signUpUser(userInfo: UserSignUpRequestDTO, onResponse: (result: CallbackResponse<UserSignUpResponseDTO>) -> Unit) {
        ServiceLocator.getNetworkService().post(WebUrls.signupUserEndPointName, userInfo) { networkCallback ->
            if (networkCallback.responseStatus == Enum.responseStatusType.success) {
                try {
                    val serverResponse = mGson.fromJson(networkCallback.dataResult ?: "", BaseUserSignUpResponseDTO::class.java)
                    if (serverResponse.action == "success") {
                        UserPrefs.getInstance().putUserId(serverResponse.data?.id ?: "")
                        UserPrefs.getInstance().putUserToken(serverResponse.data?.token ?: "")
                        val userInfo = UserLoginResponseDTO()
                        userInfo.email = serverResponse.data?.email
                        userInfo.id = serverResponse.data?.id
                        userInfo.token = serverResponse.data?.token
                        userInfo.name = serverResponse.data?.name
                        userInfo.userName = serverResponse.data?.username
                        userInfo.profilePic = serverResponse.data?.profile_pic
                        UserPrefs.getInstance().putUserInfo(userInfo)
                        onResponse.invoke(CallbackResponse(dataResult = serverResponse.data ?: UserSignUpResponseDTO()))
                    }
                    else {
                        onResponse.invoke(CallbackResponse(errorMessage = serverResponse.error ?: "Unable to signup at this moment." ))
                        Log.e("API Error", serverResponse.error ?: "")
                    }
                }
                catch (exception: Exception) {
                    Log.e("Response Parsing Error", exception.localizedMessage)
                }
            }
            else {
                onResponse.invoke(CallbackResponse(errorMessage = networkCallback.errorMessage))
            }
        }
    }

    fun deleteUserAccount(onResponse: (result: CallbackResponse<String>) -> Unit) {
        ServiceLocator.getNetworkService().post(WebUrls.signupUserEndPointName, "") { networkCallback ->
            if (networkCallback.responseStatus == Enum.responseStatusType.success) {
                try {
                    var serverResponse = mGson.fromJson(networkCallback.dataResult ?: "", BaseNetworkResponse::class.java)
                    if (serverResponse.action == "success") {
                        UserPrefs.getInstance().putUserId("")
                        UserPrefs.getInstance().putUserToken("")
                        UserPrefs.getInstance().putUserInfo(UserLoginResponseDTO())
                        onResponse.invoke(CallbackResponse(dataResult = "Account deleted successfully."))
                    }
                    else {
                        onResponse.invoke(CallbackResponse(errorMessage = serverResponse.error ?: "Unable to delete account at this moment." ))
                        Log.e("API Error", serverResponse.error ?: "")
                    }
                }
                catch (exception: Exception) {
                    Log.e("Response Parsing Error", exception.localizedMessage)
                }
            }
            else {
                onResponse.invoke(CallbackResponse(errorMessage = networkCallback.errorMessage))
            }
        }
    }
}