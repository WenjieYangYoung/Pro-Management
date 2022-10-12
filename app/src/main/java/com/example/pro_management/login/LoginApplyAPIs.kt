package com.example.pro_management.login

import android.content.Context

interface ApplyAPIs {

    /**
     * Request to apply as a new user
     */
    fun Apply(
        fullname: String,
        email: String,
        passwd_sha1: String,
        skill:String,
        isManager: Boolean = false
    ){}

    /***
     * The callback of apply response
     */
    fun onApplyResult(){}
}

interface LoginAPIs{
    fun login(
        account: String,
        passwd_sha1:String,
        mod:Int
    ) {
    }

    fun onLoginResult(

    ){}
}