package com.golfApp.golfOneUnder.utls

class WebUrls {
    companion object {
        ///Base URL to access production Database that the end users will be using
        val productionBaseURL = "https://finder-appp.herokuapp.com/api/"
        ///Base URL to access staging Database that will be used for testing purposes only.
        val stagingBaseURL = ""

        val loginUserEndPointName = "login"
        val signupUserEndPointName = "signup"
        val getLoggedInUserEndPointName = "check_login"
        val deleteUserProfileEndPointName = "delete_account"
    }
}