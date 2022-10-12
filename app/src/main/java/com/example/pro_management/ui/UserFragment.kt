package com.example.pro_management.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.pro_management.R
import com.example.pro_management.user.CurrentUser
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.pro_management.util.Signout
import com.google.gson.Gson
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException


class UserFragment: BottomSheetDialogFragment() {

    val user = CurrentUser.getUser()

    lateinit var activityView:View

    companion object{
        const val TAG = "UserFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        activityView = inflater.inflate(R.layout.fragment_bottom_user_detail, container, false)

        return activityView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init(){
        activityView.findViewById<TextView>(R.id.txt_frag_user_name).setText(user.getUsername())
        activityView.findViewById<TextView>(R.id.txt_frag_user_email).setText(user.getEmail())
        var role:String
        if(user.isManager()){
            role = "Team manager"
        }else{
            role = "Member"
        }
        activityView.findViewById<TextView>(R.id.txt_frag_user_role).setText(role)
        activityView.findViewById<Button>(R.id.btn_log_off).setOnClickListener{
            onLogOff()
        }
    }

    fun onLogOff(){
        val url = "http://35.246.127.252:5000/signout"

        val req_body = FormBody.Builder().build()

        val request = Request
            .Builder()
            .addHeader("Cookie", CurrentUser.getUser().getSession())
            .url(url)
            .post(req_body)
            .build()

        OkHttpClient().newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val res:String? = response.body?.string()

                val gson = Gson()
                val responseBody = gson.fromJson(res, responseTem::class.java)
                if(responseBody.result.isNotEmpty()){
                    if(responseBody.result == "success"){
                        EventBus.getDefault().post(Signout(0, "signout"))
                    }else{
                        EventBus.getDefault().post(Signout(-1, "signout"))

                    }
                }
            }

        })
    }
}

data class responseTem(
    val result:String,
    val info:String
)