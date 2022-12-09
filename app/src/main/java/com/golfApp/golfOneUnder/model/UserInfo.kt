package com.golfApp.golfOneUnder.model

class BaseUserLoginResponseDTO {
    var action: String = ""
    var data: UserLoginResponseDTO? = null
    var error: String? = null
}
class UserLoginResponseDTO {
    var name: String? = ""
    var userName: String? = ""
    var email: String? = ""
    var id: String? = ""
    var profilePic: String? = ""
    var token: String? = ""
}

class UserSignUpRequestDTO {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var username: String = ""
}

class BaseUserSignUpResponseDTO {
    var action: String = ""
    var data: UserSignUpResponseDTO? = null
    var error: String? = null
}

class UserSignUpResponseDTO {
    var name: String? = ""
    var username: String? = ""
    var email: String? = ""
    var id: String? = ""
    var profile_pic: String? = ""
    var token: String? = ""
}