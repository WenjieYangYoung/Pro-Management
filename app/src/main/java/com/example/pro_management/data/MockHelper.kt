package com.example.pro_management.data

import android.content.Context


class MockHelper {
    public fun prepareSharedData(context: Context){
        //test data
        var sp = context.getSharedPreferences("correntUser", Context.MODE_PRIVATE)?:return
        with(sp.edit()){
            putString("userID", "123456789")
            putString("username", "koroce")
            putString("password", "IASJDIF654651")
            apply()
        }
    }
}