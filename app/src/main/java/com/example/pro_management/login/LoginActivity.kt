package com.example.pro_management.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pro_management.R
import com.example.pro_management.user.CurrentUser
import com.example.pro_management.util.MessageLoginResult
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        EventBus.getDefault().register(this)


        init()
    }

    private fun init(){
        findViewById<Button>(R.id.btn_login_confirm).setOnClickListener{
            onComfirm()
        }


        findViewById<TextView>(R.id.txt_login_sign_up).setOnClickListener{
            signUp()
        }

    }

    private fun signUp(){
        val applyIntent = Intent()
        applyIntent.setClass(this, ApplyActivity::class.java)
        startActivityForResult(applyIntent, 202)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != 202 && resultCode != 203){
            return
        }

        val result = data!!.getIntExtra("apply_res", -1)
        when(result){
            0 -> {
                val resInent = Intent()
                resInent.putExtra("login_res", result)
                setResult(201, resInent)
            }

            -1 -> {
                Toast.makeText(this, "unknown error", Toast.LENGTH_SHORT).show()
                return
            }

            -2 ->{   //back to login activity
                return
            }
        }


    }


    private fun onComfirm(){

        val account = findViewById<EditText>(R.id.edit_login_account).text.toString()
        if(account.isEmpty()){
            Toast.makeText(this, "Invalid account", Toast.LENGTH_SHORT).show()

            return
        }

        val passwd = findViewById<EditText>(R.id.edit_login_password).text.toString()
        if(!LoginHelper.isValidPasswd(findViewById<EditText>(R.id.edit_login_password).text.toString())){
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()

            return
        }



        //Request for login
        val loginNet = LoginNetwork()
        loginNet.login(account, passwd,  0)

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageLogin(event: MessageLoginResult){
        when(event.code){
            -1 ->{
                Toast.makeText(this, event.info, Toast.LENGTH_SHORT).show()
            }
            0->{
                val intent = Intent()
                intent.putExtra("login_res", 0)
                try{
                    val user = CurrentUser.getUser()
                    user.write(this)
                }catch (e:Exception){
                    print(e.message)
                }
                setResult(201, intent)
                finish()
            }
        }
    }
}