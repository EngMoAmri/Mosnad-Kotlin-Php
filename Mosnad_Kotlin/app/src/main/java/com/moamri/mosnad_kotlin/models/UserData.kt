package com.moamri.mosnad_kotlin.models

data class UserData(val name:String, val email:String, val phone:String, val role:String, val password: String?, val confirmPassword: String?, val newPassword:String?){
    companion object{
        var currentUser:UserData? = null
    }
}