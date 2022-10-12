package com.example.pro_management.login

import com.example.pro_management.user.CurrentUser
import com.example.pro_management.util.*
import com.google.gson.Gson
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException

class ApplyNetwork:ApplyAPIs{
    override fun Apply(
        fullname: String,
        email: String,
        passwd_sha1: String,
        skill: String,
        isManager: Boolean,
    ) {
        val ApiName = "apply"   // not yet implemnt at server
        val url = "http://35.246.127.252:5000/signup"

        var str_isManager:String

        if(isManager){
            str_isManager = "manager"
        }else{
            str_isManager = "member"
        }

        val res_body = FormBody.Builder()
            .add("account", email)
            .add("passwd", passwd_sha1)
            .add("username", fullname)
            .add("skills", skill)
            .add("description", "des")
            .add("role",str_isManager)
            .build()

        val request = Request.Builder()
            .url(url)
            .header("Connection", "close")
            .post(res_body)
            .build()

        OkHttpClient().newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                var res:String? = ""
                try{
                    res = response.body?.string()
                }catch (e:IOException){
                    Apply(fullname, email, passwd_sha1, skill, isManager)
                    return
                }


                val gson = Gson()
                val user = gson.fromJson(res!!, UserEntity::class.java)
                if(!res.isNullOrEmpty()){
                    if(user.result == "success"){
                        val corrUser = CurrentUser.getUser()

                        corrUser.Unlock()
                        if(corrUser.ResetUser(user.user_id, fullname, email, passwd_sha1, str_isManager)){
                            //corrUser.setSession(header)
                            EventBus.getDefault().post(MsgA(11))
                        }else{
                            EventBus.getDefault().post(MessageLoginResult(false, user.info, -1))
                        }

                    }else{
                        if(user.result == "failed" && user.info == "account name already exists"){
                            EventBus.getDefault().post(MsgA(11))
                        }
                    }
                }

            }

        })
    }
}

class LoginNetwork:  LoginAPIs {

    override fun login(account: String, passwd_sha1: String, mod:Int) {
        val ApiName = "signin"
        val url = AppConfig.getServerWithPort() + '/' + ApiName


        val reqBody = FormBody.Builder()
            .add("account", account)
            .add("passwd", passwd_sha1)
            .build()

        val request = Request.Builder()
            .url("http://35.246.127.252:5000/signin")
            .header("Connection", "close")
            .post(reqBody)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    println(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    var res:String? = ""
                    try{
                        res = response.body?.string()
                    }catch (e:IOException){
                        login(account, passwd_sha1, mod)
                        return
                    }

                    val header = response.header("Set-Cookie")

                    if(header.isNullOrEmpty()){
                        return
                    }

                    val gson = Gson()
                    val user = gson.fromJson(res, UserEntity::class.java)

                    if(res != null){
                        if(user.result == "success"){
                            val corrUser = CurrentUser.getUser()


                            if(mod==0) {

                                corrUser.Unlock()
                                if (corrUser.ResetUser(
                                        user.user_id,
                                        user.username,
                                        account,
                                        passwd_sha1,
                                        user.role
                                    )
                                ) {
                                    corrUser.setSession(header)
                                    EventBus.getDefault()
                                        .post(MessageLoginResult(true, "success", 0))
                                } else {
                                    print(("resetfaield"))
                                }
                            }else if(mod == 2){
                                corrUser.Unlock()
                                if (corrUser.ResetUser(
                                        user.user_id,
                                        user.username,
                                        account,
                                        passwd_sha1,
                                        user.role
                                    )
                                ) {
                                    corrUser.setSession(header)

                                    EventBus.getDefault().post(MessageLoginResult(true, "success", 2))
                                } else {
                                    print(("resetfaield"))
                                }

                            }else{


                                corrUser.Unlock()
                                if(corrUser.ResetUser(user.user_id, user.username, account, passwd_sha1, user.role)){
                                    EventBus.getDefault().post(AutoLoginRes("success", 0))
                                    corrUser.setSession(header)
                                    println("*********back login success**********")
                                }else{
                                    print(("resetfaield"))
                                }
                            }
                        }           // Sign in succeed, reset current user
                        else{
                            // Sign in failed
                            EventBus.getDefault().post(MessageLoginResult(false, user.info, -1))
                        }
                    }else{
                    }

                }
            })
    }
}

data class UserEntity(

    val result:String,

    val user_id:Int,

    val info:String = "",

    val username:String,

    val role:String,
)