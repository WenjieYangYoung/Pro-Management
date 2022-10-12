package com.example.pro_management.user

import android.content.Context
import android.content.SharedPreferences
import java.lang.Exception

//Singleton
class CurrentUser private constructor(){
    init{

    }




    companion object{
        private var instance: CurrentUser? = null
            get(){
                if(field == null){
                    field = CurrentUser()
                }
                return field
            }

        fun getUser(): CurrentUser {
            return instance!!
        }
    }

    public fun Unlock(){
        isLocked = false

//        Timer().schedule(object: TimerTask(){
//            override fun run() {
//                  isLocked = true
//            }
//        }, 3)
    }

    //reset current user info for login or change user
    public fun ResetUser(
        newID:Int,
        newUsername:String,
        newEmail:String,
        newPass:String,
        newRole:String): Boolean{
        if(isLocked){
            return false
        }

        userID = newID
        username = newUsername
        email = newEmail
        password = newPass
        isManager = (newRole == "manager")


        try{
            //TODO

        }catch (ex: Exception){
            return false
        }

        isReady = true

        return isReady
    }

    fun setSession(newSession:String){
        session = newSession
    }

    /**
     * write to the SharedPreferences
     */
    public fun write(context: Context):Boolean{
        val sps: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

        val role:String
        if(isManager){
            role="Team Manager"
        }else{                // note the role of user
            role="Member"
        }

        try{
            val editor:SharedPreferences.Editor=sps.edit()
            editor.putString("email", email)
            editor.putString("passwd", password)
            editor.putString("username",username)
            editor.putString("isManager", role)
            editor.putInt("userId", userID)
            editor.apply()
        }catch (e:Exception){          // write the SharedPreferences
            print(e.message)
            return false
        }

        return true
    }

    public fun signout(context: Context):Boolean{
        var sps :SharedPreferences  =context.getSharedPreferences("user", Context.MODE_PRIVATE)

        try{
            val editor:SharedPreferences.Editor=sps.edit()
            editor.putString("email", "")
            editor.putString("passwd", "")
            editor.putString("username","")
            editor.putBoolean("isManager", false)
            editor.putInt("userId", -1)
            editor.apply()

            setSession("")
        }catch (e:Exception){
            println("**************Exception "+e.message+" ******************")
            return false
        }

        return true
    }

    private var isReady:Boolean = false

    private var session:String = ""

    private var isLocked: Boolean = true

    private var isMember:Boolean = true

    private var isManager:Boolean = false

    private var userID: Int = 0

    private lateinit var username:String

    private lateinit var password:String

    private lateinit var email:String

    private lateinit var role:String


    public fun getUsername(): String{
        return username
    }

    public fun getEmail(): String{
        return email ?: "example@email.com"
    }

    public fun isMember():Boolean{
        return isMember
    }

    public fun isManager():Boolean{
        return isManager
    }

    public fun getRole():String{
        return role
    }

    public fun getSession():String{
        return session
    }
}