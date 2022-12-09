import android.content.Context
import android.util.Log
import com.golfApp.golfOneUnder.OneFinderApplication
import com.golfApp.golfOneUnder.model.DeviceInfoDTO
import com.golfApp.golfOneUnder.model.UserLoginResponseDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nutspace.nut.api.model.BleDevice
import java.lang.Exception

class UserPrefs {

    companion object {
        private var instance: UserPrefs? = null
        fun getInstance() : UserPrefs
        {
            if (instance == null) {
                instance = UserPrefs()
            }
            return instance!!
        }
    }

    //Pref Name
    private val sharedPreferenceName = "OneFinderPrefs"
    //Keys name
    private val userAuthKey =  "userAuthKey"
    private val userIDKey =  "userIDKey"
    private val userInfoKey = "userInfoKey"
    private val deviceInfoListKey = "deviceInfoListKey"


    private fun putString(key: String, value: String) {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        val preferenceEditor = sharedPreference.edit()
        preferenceEditor.putString(key, value)
        preferenceEditor.commit()
    }

    private fun getString(key: String, defaultValue: String): String {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        return sharedPreference.getString(key, defaultValue) ?: defaultValue
    }

    private fun putInt(key: String, value: Int) {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        val preferenceEditor = sharedPreference.edit()
        preferenceEditor.putInt(key, value)
        preferenceEditor.commit()
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        return sharedPreference.getInt(key, defaultValue) ?: defaultValue
    }

    private fun putFloat(key: String, value: Float) {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        val preferenceEditor = sharedPreference.edit()
        preferenceEditor.putFloat(key, value)
        preferenceEditor.commit()
    }

    private fun getFloat(key: String, defaultValue: Float): Float {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        return sharedPreference.getFloat(key, defaultValue) ?: defaultValue
    }

    private fun putBoolean(key: String, value: Boolean) {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        val preferenceEditor = sharedPreference.edit()
        preferenceEditor.putBoolean(key, value)
        preferenceEditor.commit()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val sharedPreference = OneFinderApplication.context().getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun putUserToken(token: String) {
        putString(userAuthKey, token)
    }

    fun getUserAuthToken(): String {
        return getString(userAuthKey, "")
    }

    fun putUserId(userId: String) {
        putString(userIDKey, userId)
    }

    fun getUserId(): String {
        return getString(userIDKey, "")
    }

    fun putUserInfo(userInfo: UserLoginResponseDTO) {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val userJson = gson.toJson(userInfo)
        putString(userInfoKey, userJson)
    }

    fun getUserInfo(userInfo: UserLoginResponseDTO): UserLoginResponseDTO? {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        return try {
            gson.fromJson(getString(userInfoKey, ""), UserLoginResponseDTO::class.java)
        }
        catch (exception: Exception) {
            null
        }
    }

    fun addUserDevice(device: DeviceInfoDTO) {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val storedJson = getString(deviceInfoListKey, "")
        val storedDeviceList = ArrayList<DeviceInfoDTO>()
        try {
            gson.fromJson(storedJson, Array<DeviceInfoDTO>::class.java).toCollection(storedDeviceList)
        }
        catch (exception: Exception) {
            Log.e("Parsing error", exception.localizedMessage)
        }
        storedDeviceList.add(device)

        val deviceListJson = gson.toJson(storedDeviceList)
        putString(deviceInfoListKey, deviceListJson)
    }

    fun getDeviceList(): ArrayList<DeviceInfoDTO> {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val storedJson = getString(deviceInfoListKey, "")
        val storedDeviceList = ArrayList<DeviceInfoDTO>()
        try {
            gson.fromJson(storedJson, Array<DeviceInfoDTO>::class.java).toCollection(storedDeviceList)
        }
        catch (exception: Exception) {
            Log.e("Parsing error", exception.localizedMessage)
        }
        return storedDeviceList
    }
}