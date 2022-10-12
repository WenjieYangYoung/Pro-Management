package com.example.pro_management.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.pro_management.R
import com.example.pro_management.util.MessageLoginResult
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Waiting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        EventBus.getDefault().register(this)


        textView = findViewById<TextView>(R.id.wait_txt)


    }
    lateinit var textView: TextView

    lateinit var handler:Handler

    var dire = true
    val len = 12
    var index = 1

    fun animation(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onReceive(event: MessageLoginResult){
        if(event.code == 255){
            //val intent = Intent()
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }
}

