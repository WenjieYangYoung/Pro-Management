package com.example.pro_management.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.pro_management.R
import com.example.pro_management.util.AppConfig
import com.example.pro_management.util.MessageLoginResult
import com.example.pro_management.util.MessageRequest
import com.example.pro_management.util.MsgA
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ApplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)
        EventBus.getDefault().register(this)

        init()
    }


    private fun init(){
        //EventBus.getDefault().register(this)
        loadTag()


        findViewById<Button>(R.id.btn_apply_confirm).setOnClickListener{
            onConfirm()
        }
        findViewById<Button>(R.id.btn_apply_back).setOnClickListener({
            onBack()
        })
    }

    private fun onConfirm():Boolean{
        if(!infoChecked()){
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show()
            return false
        }


        val fullname = findViewById<EditText>(R.id.edit_apply_fullname).text.toString()
        val email = findViewById<EditText>(R.id.edit_apply_email).text.toString()
        val skill = findViewById<TextView>(R.id.edit_apply_skills).text.toString()
        val passwd = findViewById<EditText>(R.id.edit_apply_password).text.toString()
        val isManger = findViewById<Switch>(R.id.apply_switch_role).isChecked

        //val passwd_sha1 = LoginHelper.SHA1(passwd)

        val skill_ls = skill.substring(7, skill.length-1).split(' ')
        var skill_num_str = ""
        for(i in skill_ls){
            for(j in skill_map){
                if(i == j.first){
                    skill_num_str += j.third.toString()
                    if(skill_ls.indexOf(i) != skill_ls.count()-1){
                        skill_num_str += ", "
                    }
                    break
                }
            }
        }

        val applyNetwork = ApplyNetwork()
        applyNetwork.Apply(fullname, email, passwd, skill_num_str, isManger)


        return true
    }

    private fun infoChecked():Boolean{
        val edit_fullname = findViewById<EditText>(R.id.edit_apply_fullname)
        if(edit_fullname.text.toString().isEmpty()){
            Toast.makeText(this, "Invalid fullname", Toast.LENGTH_SHORT).show()

            return false
        }

        val edit_email = findViewById<EditText>(R.id.edit_apply_email)
        if(!LoginHelper.isValidEmail(edit_email.text.toString())){
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()

            return false
        }

        val edit_passwd = findViewById<EditText>(R.id.edit_apply_password)
        if(!LoginHelper.isValidPasswd(edit_passwd.text.toString())){
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()

            return false
        }

        val edit_pass_conf = findViewById<EditText>(R.id.edit_apply_password_confirm)
        if(edit_passwd.text.toString() != edit_pass_conf.text.toString()){
            Toast.makeText(this, "Two passwords are different", Toast.LENGTH_SHORT).show()

            return false
        }

        val edit_skill = findViewById<TextView>(R.id.edit_apply_skills)
        if(edit_skill.text.toString().isEmpty()){
            Toast.makeText(this, "Please choose skills", Toast.LENGTH_SHORT).show()

            return false
        }

        return true
    }

    private fun onBack(){
        val intent = Intent()
        intent.putExtra("apply_res", -2)     //normal back
        setResult(203, intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)

    }

    var skill_list = mutableListOf<String>()

    @SuppressLint("ResourceAsColor")
    private fun onTag(view: TextView){
        if (view.text.toString() in skill_list){
            skill_list.remove(view.text.toString())
            view.setTextColor(R.color.tag_bg)
            with(view){
                //setTextColor(0xE1E3E5)

                setBackgroundResource(R.drawable.inline_tag)
            }


        }else{
            if(skill_list.count() >=3){
                Toast.makeText(this, "Please choose no more than 3 skills", Toast.LENGTH_SHORT).show()
                return
            }

            skill_list.add((view.text.toString()))
            view.setTextColor(R.color.tag_text_choosed)
            with(view){

                setBackgroundResource(R.drawable.inline_tag_choosed)
            }
        }

        reloadSkills()
    }

    private fun reloadSkills(){
        val skill = findViewById<TextView>(R.id.edit_apply_skills)
        var content = "Skill: "

        for(i in skill_list){
            content += i
            content += ' '
        }

        skill.text = content
    }

    fun loadTag(){
        for(i in skill_map){
            findViewById<TextView>(i.second).setOnClickListener(object : View.OnClickListener{
                override fun onClick(view: View?) {
                    val textView = view as TextView
                    onTag(textView)
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageLogin(event: MsgA){
        if(event.code == 11){
            val intent = Intent()
            intent.putExtra("apply_res", 0)
            setResult(203, intent)
            finish()
        }
    }

    val skill_map = listOf(
        Triple<String, Int, Int>("Android", R.id.apply_skill_android, 0),
        Triple<String, Int, Int>("Java", R.id.apply_skill_java, 1),
        Triple<String, Int, Int>("Kotlin", R.id.apply_skill_kotlin, 2),
        Triple<String, Int, Int>("C#", R.id.apply_skill_csharp, 3),
        Triple<String, Int, Int>("HTML", R.id.apply_skill_html, 4),
        Triple<String, Int, Int>("JavaScript", R.id.apply_skill_js, 5),
        Triple<String, Int, Int>("C++", R.id.apply_skill_cpp, 6)
    )


}